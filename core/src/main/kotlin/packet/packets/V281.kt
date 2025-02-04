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
 * 植物探险植物选取
 *
 * @property ic [TODO]
 * @property pl 选取植物列表
 * @property wi 探险世界 id
 */
@PacketIdentifier("V281")
class V281(override val user: User) : Packet() {
    override val identifier: String = "V281"

    /**
     * [TODO]
     *
     * ```
     * ic = "0"
     * ```
     */
    var ic: String = "0"

    /**
     * 选取植物列表
     *
     * ```
     * pl = [1, 2, 3, 4, 5]
     * ```
     */
    var pl: List<Int> = listOf(1, 2, 3, 4, 5)

    /**
     * 探险世界 id
     *
     * ```
     * wi = "10427"
     * ```
     */
    var wi: String = "10427"

    @Serializable
    data class Data(
        @SerialName("ic") val ic: String,
        @SerialName("pi") val pi: String,
        @SerialName("pl") val pl: List<Int>,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
        @SerialName("wi") val wi: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            ic = ic,
            pl = pl,
            wi = wi,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

