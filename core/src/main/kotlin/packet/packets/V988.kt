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
 * 植物培育完成任务
 *
 * @property taskId 拓维标准任务 id
 */
@PacketIdentifier("V988")
class V988(override val user: User) : Packet() {
    override val identifier: String = "V988"

    /**
     * 拓维标准任务 id
     *
     * ```
     * taskId = "1001"
     * ```
     */
    var taskId: String = "1001"

    @Serializable
    data class Data(
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("t") val t: String = "0",
        @SerialName("task_id") val taskId: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            taskId = taskId,
            ui = user.credential.userId,
        )
    ).jsonObject
}

