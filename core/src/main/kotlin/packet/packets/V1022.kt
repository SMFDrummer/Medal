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
 * 未来礼盒选择奖励
 *
 * @property ids 未来礼盒选择奖励配置
 */
@PacketIdentifier("V1022")
class V1022(override val user: User) : Packet() {
    override val identifier: String = "V1022"

    /**
     * 未来礼盒选择奖励配置
     *
     * ```
     * ids = "0,1,2,3,4,5"
     * ```
     */
    var ids: String = "0,1,2,3,4,5"

    @Serializable
    data class Data(
        @SerialName("ids") val ids: String,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            ids = ids,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

