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
 * 幸运许愿池抽奖
 *
 * @property t 抽奖类型
 */
@PacketIdentifier("V870")
class V870(override val user: User) : Packet() {
    override val identifier: String = "V870"

    /**
     * 抽奖类型
     *
     * ```
     * t = "0" // 单抽
     * t = "1" // 十连抽
     * ```
     */
    lateinit var t: String

    @Serializable
    data class Data(
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("t") val t: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            t = t,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}