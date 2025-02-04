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
 * 双人对决启动游戏
 *
 * @suppress 自定义, 两个属性只要改动都会无法进入
 */
@PacketIdentifier("V823")
class V823(override val user: User) : Packet() {
    override val identifier: String = "V823"

    @Serializable
    data class Data(
        @SerialName("pi") val pi: String,
        @SerialName("progress") val progress: String = "100",
        @SerialName("sk") val sk: String,
        @SerialName("start") val start: String = "1",
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

