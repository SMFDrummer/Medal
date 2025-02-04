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
 * 七日指南任务领取及累计奖励
 *
 * @property i 拓维标准任务 id
 * @property t 领取状态
 */
@PacketIdentifier("V951")
class V951(override val user: User) : Packet() {
    override val identifier: String = "V951"

    /**
     * 拓维标准任务 id
     *
     * ```
     * i = "1001" // 任务领取
     * i = "0" // 累计奖励
     * ```
     */
    var i: String = "0"

    /**
     * 领取状态
     *
     * ```
     * t = "1" // 任务领取
     * t = "2" // 累计奖励
     * ```
     */
    var t: String = "2"

    @Serializable
    data class Data(
        @SerialName("ai") val ai: String = "10828", // 活动 id
        @SerialName("i") val i: String,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("t") val t: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            i = i,
            t = t,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

