package manager

import api.logger
import config.IOSConfig
import config.StrategyConfig
import getIntValue
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.*
import modify
import packet.Crypto
import packet.request
import parseObject
import service.model.User
import service.model.packets
import primitive

object StrategyManager {
    private lateinit var strategy: StrategyConfig

    private fun init(strategy: StrategyConfig) {
        this.strategy = strategy
    }

    suspend fun StrategyConfig.execute(user: User, lastResponseHandler: ((JsonObject) -> Unit)? = null): Boolean {
        init(this)
        logger.info("执行策略: $strategy")
        var response = buildJsonObject {  }
        suspend fun executePacketCycle(packet: StrategyConfig.PacketConfig): Boolean {
            for (i in packet.cycle downTo 1) {
                user.packets[packet.i]?.let { _packet ->
                    val modifiedPacket = with(_packet.build()) {
                        if (config is IOSConfig) {
                            packet.t.put("ver_", primitive { config.channel.version })
                        }
                        first to second.modify(
                            *packet.t.entries.map { it.key to it.value }.toTypedArray()
                        )
                    }
                    println(modifiedPacket)
                    response = try {
                        Crypto.Response.decrypt(Crypto.Request.encrypt(modifiedPacket).request(config.host)).parseObject()
                    } catch (e: Exception) {
                        logger.error("Decryption or request failed for packet ${packet.i}, error: ${e.message}\n${e.stackTraceToString()}")
                        return false
                    }
                    println(response)
                    if (response.getIntValue("r") != packet.r) {
                        logger.error("Mismatched response: $response")
                        return false
                    }
                }
            }
            return true
        }

        suspend fun handleRetry(packet: StrategyConfig.PacketConfig): Boolean {
            var success = false
            repeat(packet.retry) { attempt ->
                try {
                    success = executePacketCycle(packet)
                    if (success) return@repeat
                } catch (e: Exception) {
                    logger.error("Attempt $attempt failed with exception: ${e.message}")
                }
            }
            return success
        }

        suspend fun execute(startIndex: Int = 0): JsonPrimitive {
            for (packet in strategy.packets.drop(startIndex)) {
                if (packet.cycle <= 0) continue
                var success = executePacketCycle(packet)
                if (!success) success = handleRetry(packet)
                return if (success) {
                    packet.onSuccess ?: continue
                } else {
                    packet.onFailure ?: continue
                }
            }
            return primitive { true }
        }

        suspend fun handleAction(jsonPrimitive: JsonPrimitive): Boolean = try {
            val content = jsonPrimitive.content
            val intValue = content.toIntOrNull()
            when {
                intValue != null -> {
                    handleAction(execute(intValue - 1))
                }
                content.equals("true", ignoreCase = true) || content.equals("false", ignoreCase = true) -> {
                    content.toBoolean()
                }
                else -> error("Unexpected value $content")
            }
        } catch (e: Exception) {
            logger.error("Error handleAction: ${e.message}")
            false
        }

        return user.scope.async {
            user.lock.withLock {
                handleAction(execute(0))
            }
        }.await().apply {
            lastResponseHandler?.let { it(response) }
        }
    }
}