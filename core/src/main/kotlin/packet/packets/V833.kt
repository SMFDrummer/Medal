package packet.packets

import JsonFeature
import by
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import packet.Packet
import service.model.User
import packet.PacketIdentifier
/**
 * 双人对决出战僵尸上传
 *
 * @suppress 自定义
 */
@PacketIdentifier("V833")
class V833(override val user: User) : Packet() {
    override val identifier: String = "V833"

    @Serializable
    data class Data(
        @SerialName("fight") val fight: String = "400000, 400001, 400002, 400003, 400004, 400005, 400006, 400007",
        @SerialName("fightIndex") val fightIndex: String = "0",
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

