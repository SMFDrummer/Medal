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
 * 创意庭院关卡通关
 *
 * @property id 关卡号
 * @property s [TODO]
 */
@PacketIdentifier("V721")
class V721(override val user: User) : Packet() {
    override val identifier: String = "V721"

    /**
     * 关卡号
     *
     * ```
     * id = "27703998"
     * ```
     */
    var id: String = "27703998"

    /**
     * [TODO]
     *
     * ```
     * s = "107"
     * ```
     */
    var s: String = "107"

    @Serializable
    data class Data(
        @SerialName("f") val f: String = "0",
        @SerialName("id") val id: String,
        @SerialName("pi") val pi: String,
        @SerialName("s") val s: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            id = id,
            s = s,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

