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
 * 秘境通关
 *
 * @property level 关卡
 * @property tgt [TODO]
 * @property type 关卡状态
 * @property world 秘境世界名称
 */
@PacketIdentifier("V411")
class V411(override val user: User) : Packet() {
    override val identifier: String = "V411"

    /**
     * 关卡
     *
     * ```
     * level = "0"
     * ```
     */
    var level: String = "0"

    /**
     * [TODO]
     *
     * ```
     * tgt = "" // "1, 1, 1" 或 "15" 等
     * ```
     */
    var tgt: String = ""

    /**
     * 关卡状态
     *
     * ```
     * type = "0" // 0 ~ 4
     * ```
     */
    var type: String = "0"

    /**
     * 秘境世界名称
     *
     * ```
     * world = "uncharted_childrensday" // 儿童节
     * world = "uncharted_tale" // 童话世界第一秘境
     * world = "uncharted_tale_2" // 童话世界第二秘境
     * world = "uncharted_needforspeed_202406" // 飞车秘境
     * ```
     */
    var world: String = ""

    @Serializable
    data class Data(
        @SerialName("level") val level: String,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("tgt") val tgt: String,
        @SerialName("type") val type: String,
        @SerialName("ui") val ui: String,
        @SerialName("win") val win: String = "1",
        @SerialName("world") val world: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            level = level,
            tgt = tgt,
            type = type,
            world = world,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}