package packet.packets

import JsonFeature
import by
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import manager.config
import packet.Packet
import service.model.User
import packet.PacketIdentifier
/**
 * 使用兑换码
 *
 * @property c 兑换码
 */
@PacketIdentifier("V330")
class V330(override val user: User) : Packet() {
    override val identifier: String = "V330"

    /**
     * 兑换码
     *
     * ```
     * c = " /* 兑换码 */ "
     * ```
     */
    var c: String = ""

    @Serializable
    data class Data(
        @SerialName("c") val c: String,
        @SerialName("ch") val ch: String = config.channel.packageName,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            c = c,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

