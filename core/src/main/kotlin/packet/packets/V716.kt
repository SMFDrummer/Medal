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
 * 龙族宝库积分兑换
 *
 * @property key 积分数量
 */
@PacketIdentifier("V716")
class V716(override val user: User) : Packet() {
    override val identifier: String = "V716"

    /**
     * 积分数量
     *
     * ```
     * key = "2"
     * ```
     */
    var key: String = "2"

    @Serializable
    data class Data(
        @SerialName("key") val key: String,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            key = key,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}