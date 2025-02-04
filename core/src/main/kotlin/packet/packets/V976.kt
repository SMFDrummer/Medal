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
 * 回忆成就完成奖励
 *
 * @property ctp 完成奖励状态码
 * @property i 拓维标准任务 id
 */
@PacketIdentifier("V976")
class V976(override val user: User) : Packet() {
    override val identifier: String = "V976"

    /**
     * 完成奖励状态码
     *
     * ```
     * ctp = "0" // 领取奖励
     * ctp = "1" // 完成成就
     * ```
     */
    var ctp: String = "0"

    /**
     * 拓维标准任务 id
     *
     * ```
     * i = "1010" // 完成成就
     * i = "1" // 领取奖励
     * ```
     */
    var i: String = "1"

    @Serializable
    data class Data(
        @SerialName("ctp") val ctp: String,
        @SerialName("i") val i: String,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("tp") val tp: String = "10839", // 回忆成就 id
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            ctp = ctp,
            i = i,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

