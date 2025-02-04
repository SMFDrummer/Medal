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
 * 使用植物体验卡
 *
 * @property id 植物体验卡 id
 * @property num 使用数量
 */
@PacketIdentifier("V521")
class V521(override val user: User) : Packet() {
    override val identifier: String = "V521"

    /**
     * 植物体验卡 id
     *
     * ```
     * id = "97020"
     * ```
     */
    var id: String = "97020"

    /**
     * 使用数量, 默认为 1
     *
     * ```
     * num = "1"
     * ```
     */
    var num: String = "1"

    @Serializable
    data class Data(
        @SerialName("id") val id: String,
        @SerialName("num") val num: String,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            id = id,
            num = num,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

