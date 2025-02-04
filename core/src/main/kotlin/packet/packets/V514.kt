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
 * 装扮券兑换
 *
 * @property di 装扮 id
 * @property n 消耗装扮券数量
 */
@PacketIdentifier("V514")
class V514(override val user: User) : Packet() {
    override val identifier: String = "V514"

    /**
     * 装扮 id
     *
     * ```
     * di = "32000480"
     * ```
     */
    var di: String = "32000480"

    /**
     * 消耗装扮券数量
     *
     * ```
     * n = "400"
     * ```
     */
    var n: String = "400"

    @Serializable
    data class Data(
        @SerialName("di") val di: String,
        @SerialName("n") val n: String,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            di = di,
            n = n,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

