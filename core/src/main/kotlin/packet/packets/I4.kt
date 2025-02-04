package packet.packets

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import packet.Packet
import packet.PacketIdentifier

import service.model.User

/**
 * 获取当前时间
 */
@PacketIdentifier("I4")
class I4(override val user: User) : Packet() {
    override val identifier: String = "I4"

    override fun build(): Pair<String, JsonObject> = identifier to buildJsonObject {  }
}