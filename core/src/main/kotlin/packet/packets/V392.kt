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
 * 通用游戏内货币兑换道具
 *
 * @property ci 花费货币单位
 * @property qi 兑换物品 id 或对应 UI 位置
 * @property mi 兑换货币 id
 * @property q 兑换数量
 * @property si [TODO]
 */
@PacketIdentifier("V392")
class V392(override val user: User) : Packet() {
    override val identifier: String = "V392"

    /**
     * 花费货币单位
     *
     * ```
     * ci = "100"
     * ```
     */
    var ci: String = "100"

    /**
     * 兑换物品 id 或对应 UI 位置
     *
     * ```
     * gi = "22000260" // 物品 id
     * gi = "4" // 对应 UI 位置
     * ```
     */
    var gi: String = "22000260"

    /**
     * 兑换货币 id
     *
     * 23093 - [TODO]
     *
     * 23243 - 双人对决币
     *
     * 23400 - 回忆之旅普通蜗牛币
     *
     * 23402 - 创意庭院币
     *
     * ```
     * mi = "23400"
     * ```
     */
    var mi: String = "23400"

    /**
     * 兑换数量
     *
     * ```
     * q = "1"
     * ```
     */
    var q: String = "1"

    /**
     * [TODO]
     *
     * ```
     * si = "10"
     * ```
     */
    var si: String = "10"

    @Serializable
    data class Data(
        @SerialName("ci") val ci: String,
        @SerialName("gi") val gi: String,
        @SerialName("mi") val mi: String,
        @SerialName("pi") val pi: String,
        @SerialName("q") val q: String,
        @SerialName("si") val si: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            ci = ci,
            gi = gi,
            mi = mi,
            q = q,
            si = si,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId
        )
    ).jsonObject
}

