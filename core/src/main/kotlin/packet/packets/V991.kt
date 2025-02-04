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
 * 派对助力抽奖
 *
 * @property ecn 消耗奖券数量
 * @property selectGift 选择奖励
 */
@PacketIdentifier("V991")
class V991(override val user: User) : Packet() {
    override val identifier: String = "V991"

    /**
     * 消耗奖券数量
     *
     * ```
     * ecn = "3"
     * ```
     */
    var ecn: String = "3"

    /**
     * 选择奖励, 默认为空
     *
     * ```
     * selectGift = ""
     * ```
     */
    var selectGift: String = ""

    @Serializable
    data class Data(
        @SerialName("ecn") val ecn: String,
        @SerialName("pi") val pi: String,
        @SerialName("selectGift") val selectGift: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            ecn = ecn,
            pi = user.credential.personalId,
            selectGift = selectGift,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

