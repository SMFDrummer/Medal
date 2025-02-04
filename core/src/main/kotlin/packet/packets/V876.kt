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
 * 刷邀请码
 *
 * @property code 邀请码
 * @property star 用户星级
 */
@PacketIdentifier("V876")
class V876(override val user: User) : Packet() {
    override val identifier: String = "V876"

    /**
     * 邀请码
     *
     * ```
     * code = "ABCDEFGHI"
     * ```
     */
    var code: String = ""

    /**
     * 用户星级
     *
     * ```
     * star = "80"
     * ```
     */
    var star: String = "80"

    @Serializable
    data class Data(
        @SerialName("code") val code: String,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("star") val star: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            code = code,
            star = star,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject

}

