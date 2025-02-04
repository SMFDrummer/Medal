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
 * 指定活动线索领取
 *
 * @property ai 活动 id
 * @property s 线索数
 * @property ti 拓维标准任务 id
 */
@PacketIdentifier("V795")
class V795(override val user: User) : Packet() {
    override val identifier: String = "V795"

    /**
     * 活动 id
     *
     * ```
     * ai = "10749" // 宝藏线索
     * ai = "10803" // 追击指南
     * ```
     */
    var ai: String = ""

    /**
     * 线索数, 默认为 300
     *
     * ```
     * s = "300"
     * ```
     */
    var s: String = "300"

    /**
     * 拓维标准任务 id
     *
     * ```
     * ti = "1001"
     * ```
     */
    var ti: String = "1001"

    @Serializable
    data class Data(
        @SerialName("ai") val ai: String,
        @SerialName("g") val g: String = "1",
        @SerialName("pi") val pi: String,
        @SerialName("s") val s: String,
        @SerialName("sk") val sk: String,
        @SerialName("ti") val ti: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            ai = ai,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
            s = s,
            ti = ti,
        )
    ).jsonObject
}

