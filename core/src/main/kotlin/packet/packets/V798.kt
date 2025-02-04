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
 * 碎片挑战增加次数
 *
 * @property i 挑战 id
 */
@PacketIdentifier("V798")
class V798(override val user: User) : Packet() {
    override val identifier: String = "V798"

    /**
     * 挑战 id
     *
     * ```
     * i = "10502" // 巨人危机
     * i = "10503" // 邪恶入侵
     * ```
     */
    var i: String = ""

    @Serializable
    data class Data(
        @SerialName("i") val i: String,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            i = i,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}