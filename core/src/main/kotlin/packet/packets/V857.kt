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
 * 双人对决挑战任务奖励领取
 *
 * @property ti 拓维标准任务 id
 */
@PacketIdentifier("V857")
class V857(override val user: User) : Packet() {
    override val identifier: String = "V857"

    /**
     * 拓维标准任务 id
     *
     * ```
     * ti = "1001"
     * ```
     */
    var ti: String = "1001"

    /**
     * [TODO]
     * 未知状态
     *
     * ```
     * type = "0" // 每日任务？
     * type = "1" // 周任务？
     * ```
     */
    var type: String = "0"

    @Serializable
    data class Data(
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ti") val ti: String,
        @SerialName("type") val type: String,
        @SerialName("ui") val ui: String,
        @SerialName("wi") val wi: String = "0",
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ti = ti,
            type = type,
            ui = user.credential.userId,
        )
    ).jsonObject
}

