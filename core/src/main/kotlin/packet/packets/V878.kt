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
 * 邀请有礼抽奖领取
 *
 * @property bai 邀请有礼抽奖领取指定大礼
 * @property gi 固定值
 * @property type 邀请有礼抽奖或领取配置
 */
@PacketIdentifier("V878")
class V878(override val user: User) : Packet() {
    override val identifier: String = "V878"

    /**
     * 邀请有礼抽奖领取指定大礼
     *
     * ```
     * bai = null
     * bai = "5" // 魔豆神器
     * bai = "6" // 进阶书
     * bai = "7" // 万能碎片 × 20
     * ```
     */
    var bai: String? = null

    /**
     * 如果为领取则为固定值 0
     *
     * ```
     * qi = bai?.let { "0" }
     * ```
     */
    var gi: String? = bai?.let { "0" }

    /**
     * 邀请有礼抽奖或领取配置
     *
     * ```
     * type = "0" // 抽奖
     * type = "1" // 领取
     * ```
     */
    var type: String = ""

    @Serializable
    data class Data(
        @SerialName("bai") val bai: String?,
        @SerialName("gi") val gi: String?,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("type") val type: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            bai = bai,
            gi = gi,
            type = type,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

