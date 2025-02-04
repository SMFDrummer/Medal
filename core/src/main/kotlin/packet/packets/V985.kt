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
 * 时空寻宝充能任务完成领取
 *
 * @property t 任务状态
 * @property ti 拓维标准任务 id
 */
@PacketIdentifier("V985")
class V985(override val user: User) : Packet() {
    override val identifier: String = "V985"

    /**
     * 任务状态
     *
     * ```
     * t = "1"
     * ```
     */
    var t: String = "1"

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
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("t") val t: String,
        @SerialName("ti") val ti: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            t = t,
            ti = ti,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}