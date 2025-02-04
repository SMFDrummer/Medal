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
 * 绑定邮箱手机
 *
 * @property a [TODO]
 * @property em 绑定邮箱
 * @property p 绑定手机
 */
@PacketIdentifier("V310")
class V310(override val user: User) : Packet() {
    override val identifier: String = "V310"

    /**
     * [TODO]
     *
     * ```
     * a = "99"
     * ```
     */
    var a: String = "99"

    /**
     * 绑定邮箱
     *
     * ```
     * em = "sbtw@sbtw.com"
     * ```
     */
    var em: String = "sbtw@sbtw.com"

    /**
     * 绑定手机
     *
     * ```
     * p = "12345678910"
     * ```
     */
    var p: String = "12345678910"

    @Serializable
    data class Data(
        @SerialName("a") val a: String,
        @SerialName("em") val em: String,
        @SerialName("p") val p: String,
        @SerialName("pi") val pi: String,
        @SerialName("s") val s: String = "0",
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            a = a,
            em = em,
            p = p,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

