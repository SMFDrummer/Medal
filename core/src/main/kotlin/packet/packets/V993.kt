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
 * 派对助力活动领取任务奖励
 *
 * @property giftId 奖励 id
 */
@PacketIdentifier("V993")
class V993(override val user: User) : Packet() {
    override val identifier: String = "V993"

    /**
     * 奖励 id, 默认为 0
     *
     * ```
     * giftId = "0"
     * ```
     */
    var giftId: String = "0"

    @Serializable
    data class Data(
        @SerialName("giftId") val giftId: String,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            giftId = giftId,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}