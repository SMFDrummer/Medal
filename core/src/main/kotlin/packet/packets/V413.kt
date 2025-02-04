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
 * 秘境星星成就兑换
 *
 * @property index 奖励序号
 * @property world 秘境世界名称
 */
@PacketIdentifier("V413")
class V413(override val user: User) : Packet() {
    override val identifier: String = "V413"

    /**
     * 奖励序号
     *
     * ```
     * index = "0"
     * ```
     */
    var index: String = "0"

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
        @SerialName("index") val index: String,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
        @SerialName("world") val world: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            index = index,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
            world = world
        )
    ).jsonObject
}

