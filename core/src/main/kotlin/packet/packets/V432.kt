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
 * 通行证任务完成领取
 *
 * @property ti 拓维标准任务 id
 * @property type 通行证任务经验领取
 */
@PacketIdentifier("V432")
class V432(override val user: User) : Packet() {
    override val identifier: String = "V432"

    /**
     * 拓维标准任务 id
     *
     * ```
     * ti = "1001"
     * ```
     */
    var ti: String = "1001"

    /**
     * 通行证任务经验领取
     *
     * ```
     * type = "0" // 每日任务
     * type = "1" // 每周任务
     * type = "2" // 限定任务
     * ```
     */
    var type: String = "0"

    @Serializable
    data class Data(
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ti") val ti: String,
        @SerialName("type") val type: String = "0",
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ti = ti,
            ui = user.credential.userId,
        )
    ).jsonObject
}

