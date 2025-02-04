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
 * 龙族宝库选择奖励执行
 *
 * @property selectGift 选择奖励
 */
@PacketIdentifier("V714")
class V714(override val user: User) : Packet() {
    override val identifier: String = "V714"

    /**
     * 选择奖励
     *
     * ```
     * selectGift = null // 不选择即为执行获取（单抽）
     * selectGift = listOf(2, 1, 1)
     * ```
     */
    var selectGift: List<Int>? = null

    @Serializable
    data class Data(
        @SerialName("pi") val pi: String,
        @SerialName("selectGift") val selectGift: List<Int>?,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            pi = user.credential.personalId,
            selectGift = selectGift,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

