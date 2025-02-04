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
 * 超级自选选择奖励
 *
 * @property pl 奖励列表
 */
@PacketIdentifier("V853")
class V853(override val user: User) : Packet() {
    override val identifier: String = "V853"

    /**
     * 奖励列表
     *
     * ```
     * pl = listOf(1124, 23141)
     * ```
     */
    var pl: List<Int> = listOf(1124, 23141)

    @Serializable
    data class Data(
        @SerialName("bi") val bi: String = "0",
        @SerialName("pi") val pi: String,
        @SerialName("pl") val pl: List<Int>,
        @SerialName("sk") val sk: String,
        @SerialName("t") val t: String = "0",
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            pl = pl,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

