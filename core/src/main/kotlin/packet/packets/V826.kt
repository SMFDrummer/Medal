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
 * 双人对决胜利
 *
 * @property bot 机器人判定
 */
@PacketIdentifier("V826")
class V826(override val user: User) : Packet() {
    override val identifier: String = "V826"

    /**
     * 机器人判定
     *
     * ```
     * bot = "0" // 真人, 需要前置 V821
     * bot = "1" // 机器人, 无需 V821
     * ```
     */
    var bot: String = "0"

    @Serializable
    data class Data(
        @SerialName("bot") val bot: String,
        @SerialName("botTimes") val botTimes: String = "0",
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
        @SerialName("win") val win: String = "1",
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            bot = bot,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}