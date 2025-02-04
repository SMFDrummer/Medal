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
 * 秘境通关奖励兑换
 *
 * @property index 奖励序号
 * @property levelId 关卡序号
 * @property world 秘境世界名称
 */
@PacketIdentifier("V415")
class V415(override val user: User) : Packet() {
    override val identifier: String = "V415"

    /**
     * 奖励序号
     *
     * ```
     * index = "1001"
     * ```
     */
    var index: String = "1001"

    /**
     * 关卡序号
     *
     * ```
     * levelId = "0"
     * ```
     */
    var levelId: String = "0"

    /**
     * 秘境世界名称
     *
     * ```
     * world = "uncharted_childrensday" // 儿童节
     * world = "uncharted_tale" // 童话世界第一秘境
     * world = "uncharted_tale_2" // 童话世界第二秘境
     * world = "uncharted_needforspeed_202406" // 飞车秘境
     * world = "uncharted_anniversary_halloween_202411" // 万圣节活动
     * ```
     */
    var world: String = ""

    @Serializable
    data class Data(
        @SerialName("index") val index: String,
        @SerialName("levelid") val levelId: String,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
        @SerialName("world") val world: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            index = index,
            levelId = levelId,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
            world = world
        )
    ).jsonObject
}

