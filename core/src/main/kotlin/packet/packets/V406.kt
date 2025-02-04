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
 * 秘境任务
 *
 * @property t （飞车）秘境任务
 */
@PacketIdentifier("V406")
class V406(override val user: User) : Packet() {
    override val identifier: String = "V406"

    /**
     * （飞车）秘境任务
     *
     * ```
     * t = "1001" // 1001 ~ 1020
     * ```
     */
    var t: String = "1001"

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