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
import packet.PacketIdentifier
import service.model.User

/**
 * 购买物品
 *
 * @property oi 购买物品 id
 * @property q 购买物品数量
 * @property si [TODO]
 */
@PacketIdentifier("V209")
class V209(override val user: User) : Packet() {
    override val identifier: String = "V209"

    /**
     * 购买物品 id
     *
     * ```
     * oi = "12345"
     * ```
     */
    var oi: String = "12345"

    /**
     * 购买物品数量
     *
     * ```
     * q = "1"
     * ```
     */
    var q: String = "1"

    /**
     * [TODO]
     *
     * ```
     * si = "1"
     * ```
     */
    var si: String = "1"

    @Serializable
    data class Data(
        @SerialName("oi") val oi: String,
        @SerialName("pi") val pi: String,
        @SerialName("q") val q: String,
        @SerialName("si") val si: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
        @SerialName("uk") val uk: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            oi = oi,
            q = q,
            si = si,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
            uk = "${user.uk}"
        )
    ).jsonObject
}

