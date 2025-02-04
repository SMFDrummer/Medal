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
 * 幸运许愿池选择奖励领取
 *
 * @property wl 选择奖励
 */
@PacketIdentifier("V872")
class V872(override val user: User) : Packet() {
    override val identifier: String = "V872"

    /**
     * 选择奖励
     *
     * ```
     * wl = listOf(listOf(2, 2, 1), listOf(2, 2, 1), listOf(1, 1, 0))
     * ```
     */
    var wl: List<List<Int>> = listOf(listOf(2, 2, 1), listOf(2, 2, 1), listOf(1, 1, 0))

    @Serializable
    data class Data(
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
        @SerialName("wl") val wl: List<List<Int>>,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
            wl = wl
        )
    ).jsonObject
}

