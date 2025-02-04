package service.model

import config.AndroidConfig
import config.IOSConfig
import getInt
import getIntValue
import getString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.Required
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import manager.StrategyManager.execute
import manager.config
import packet.Packet
import packet.Registry
import packet.model.Credential
import strategy.androidCredential
import strategy.iosCredentialInit
import strategy.iosCredentialNonRandom
import strategy.iosCredentialRandom

@Serializable
data class User(
    @Required val userId: JsonPrimitive,
    val userNick: String? = null,
    val token: String? = null,
    val phone: String? = null,
    var password: String? = null,
    var activate: Boolean = true,
    var banned: Boolean = false
) {
    @Transient
    val scope = CoroutineScope(Dispatchers.IO)

    @Transient
    val lock = Mutex()

    @Transient
    lateinit var credential: Credential

    @Transient
    var uk = 1001
        get() = ++field

    fun refreshCredential(isRandom: Boolean = false) = scope.launch {
        when (config) {
            AndroidConfig -> androidCredential().execute(this@User) {
                when (it.getIntValue("r")) {
                    0 -> {
                        credential = Credential(
                            personalId = it["d"]?.jsonObject?.getString("ui")!!,
                            securityKey = it["d"]?.jsonObject?.getString("sk")!!,
                            userId = it["d"]?.jsonObject?.getString("ui")!!
                        )
                    }

                    20507 -> throw UserBannedException("${it.getString("d")}")
                    else -> error("获取用户凭据失败 (${it.getInt("r")}): ${it.getString("d")}")
                }
            }
            IOSConfig -> {
                var sk = ""
                var ui = ""
                iosCredentialInit().execute(this@User) {
                    when (it.getIntValue("r")) {
                        0 -> {
                            sk = it["d"]?.jsonObject?.getString("sk")!!
                            ui = it["d"]?.jsonObject?.getString("ui")!!
                        }

                        20507 -> throw UserBannedException("${it.getString("d")}")
                        else -> error("获取用户凭据失败 (${it.getInt("r")}): ${it.getString("d")}")
                    }
                }
                when (isRandom) {
                    true -> iosCredentialRandom(sk, ui)
                    false -> iosCredentialNonRandom(sk, ui)
                }.execute(this@User) {
                    when (it.getIntValue("r")) {
                        0 -> {
                            credential = Credential(
                                personalId = when (isRandom) {
                                    true -> it["d"]?.jsonObject?.getString("pi")!!
                                    false -> it["d"]?.jsonObject["bprl"]?.jsonArray[0]?.jsonObject?.getString("pi")!!
                                },
                                securityKey = sk,
                                userId = ui
                            )
                        }

                        20507 -> throw UserBannedException("${it.getString("d")}")
                        else -> error("获取用户凭据失败 (${it.getInt("r")}): ${it.getString("d")}")
                    }
                }
            }
        }
    }

    fun shutdown(message: String) {
        scope.cancel(message)
    }
}

val User.packets: Map<String, Packet?>
    get() = object : Map<String, Packet?> {
        override val entries: Set<Map.Entry<String, Packet?>>
            get() = Registry.getAllPackets(this@packets).entries
        override val keys: Set<String>
            get() = Registry.getAllPackets(this@packets).keys
        override val size: Int
            get() = Registry.getAllPackets(this@packets).size
        override val values: Collection<Packet?>
            get() = Registry.getAllPackets(this@packets).values

        override fun containsKey(key: String): Boolean = Registry.getPacket(key, this@packets) != null
        override fun containsValue(value: Packet?): Boolean = Registry.getAllPackets(this@packets).containsValue(value)
        override fun get(key: String): Packet? = Registry.getPacket(key, this@packets)
        override fun isEmpty(): Boolean = Registry.getAllPackets(this@packets).isEmpty()
    }

class UserBannedException(override val message: String?) : Exception(message)