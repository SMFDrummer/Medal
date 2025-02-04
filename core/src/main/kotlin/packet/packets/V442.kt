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
 * 秘境重置关卡刷新
 *
 * @property world 秘境世界名称
 */
@PacketIdentifier("V442")
class V442(override val user: User) : Packet() {
    override val identifier: String = "V442"

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
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("type") val type: String = "0",
        @SerialName("ui") val ui: String,
        @SerialName("world") val world: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            world = world,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

