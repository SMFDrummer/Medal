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
 * 双人对决匹配对手
 *
 * @property maps 地图
 * @property plants 植物
 */
@PacketIdentifier("V821")
class V821(override val user: User) : Packet() {
    override val identifier: String = "V821"

    /**
     * 地图, 默认为 "1, 2, 3"
     *
     * ```
     * maps = "1, 2, 3"
     * ```
     */
    var maps: String = "1, 2, 3"

    /**
     * 植物, 数量为 6
     *
     * ```
     * plants = "111033, 200060, 200034, 111035, 111029, 111016"
     * ```
     */
    var plants: String = "111033, 200060, 200034, 111035, 111029, 111016"

    @Serializable
    data class Data(
        @SerialName("maps") val maps: String,
        @SerialName("pi") val pi: String,
        @SerialName("plants") val plants: String,
        @SerialName("sk") val sk: String,
        @SerialName("t") val t: String = "1",
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            maps = maps,
            pi = user.credential.personalId,
            plants = plants,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

