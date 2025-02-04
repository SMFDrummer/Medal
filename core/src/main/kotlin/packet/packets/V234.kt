package packet.packets

import JsonFeature
import by
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import packet.Crypto
import packet.Packet
import packet.PacketIdentifier
import service.model.User

/**
 * IOS 初始化账号?
 */
@PacketIdentifier("V234")
class V234(override val user: User): Packet() {
    override val identifier: String = "V234"

    /**
     * IOS di 校验，udid 的 MD5 字符串
     *
     * ```
     * di = Crypto.getMD5(user.userId.content)
     * ```
     */
    var di = Crypto.getMD5(user.userId.content)

    @Serializable
    data class Data(
        @SerialName("di") val di: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            di = di
        )
    ).jsonObject
}