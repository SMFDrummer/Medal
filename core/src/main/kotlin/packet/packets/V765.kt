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
 * 超 Z 联赛看广告获取奖励
 *
 * @property id 奖励状态
 */
@PacketIdentifier("V765")
class V765(override val user: User) : Packet() {
    override val identifier: String = "V765"

    /**
     * 奖励状态
     *
     * ```
     * id = "1" // 10 钻石
     * id = "4" // 超 Z 钥匙
     * ```
     */
    var id: String = "1"

    @Serializable
    data class Data(
        @SerialName("id") val id: String,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            id = id,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

