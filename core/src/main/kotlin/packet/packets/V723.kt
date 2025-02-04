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
 * 创意庭院获取整个游玩页面
 *
 * @suppress 自定义
 */
@PacketIdentifier("V723")
class V723(override val user: User) : Packet() {
    override val identifier: String = "V723"

    @Serializable
    data class Data(
        @SerialName("c") val c: String = "100",
        @SerialName("f") val f: String = "0",
        @SerialName("k") val k: String = "0",
        @SerialName("pi") val pi: String,
        @SerialName("pk") val pk: String = "0,1,2,3,4,5,6,7",
        @SerialName("s") val s: String = "0",
        @SerialName("sk") val sk: String,
        @SerialName("t") val t: String = "3",
        @SerialName("ui") val ui: String,
        @SerialName("w") val w: String = "0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19",
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

