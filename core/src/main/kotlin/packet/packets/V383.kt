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
 * 超 Z 联赛商店兑换道具
 *
 * @property oi 奖励 id
 * @property q 消耗超 Z 币数量
 */
@PacketIdentifier("V383")
class V383(override val user: User) : Packet() {
    override val identifier: String = "V383"

    /**
     * 奖励 id
     *
     * ```
     * oi = "1234"
     * ```
     */
    var oi: String = "1234"

    /**
     * 消耗超 Z 币数量
     *
     * ```
     * q = "10"
     * ```
     */
    var q: String = "10"

    @Serializable
    data class Data(
        @SerialName("oi") val oi: String,
        @SerialName("pi") val pi: String,
        @SerialName("q") val q: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            oi = oi,
            q = q,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId
        )
    ).jsonObject
}

