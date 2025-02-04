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
 * 免费更换名字
 *
 * @property n 名字
 */
@PacketIdentifier("V312")
class V312(override val user: User) : Packet() {
    override val identifier: String = "V312"

    /**
     * 名字
     *
     * ```
     * n = "勤劳的向日葵"
     * ```
     */
    var n: String = "勤劳的向日葵"

    @Serializable
    data class Data(
        @SerialName("n") val n: String,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            n = n,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

