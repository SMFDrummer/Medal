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
 * 获取植物道具通用数据包
 *
 * @property pl 奖励列表
 */
@PacketIdentifier("V900")
class V900(override val user: User) : Packet() {
    override val identifier: String = "V900"

    /**
     * 新手植物列表
     *
     * ```
     * pl = listOf(Data.Pl(1001, 1), Data.Pl(1002, 1), Data.Pl(1003, 1)) // 新手植物
     * pl = listOf(Data.Pl(200043, 1)) // 石楠花
     * pl = listOf(Data.Pl(23097, 50)) // 创意庭院游玩奖励游玩币
     * pl = listOf(Data.Pl(23097, 100)) // 通关获取红水晶
     * ```
     */
    var pl: List<Data.Pl> = listOf()

    @Serializable
    data class Data(
        @SerialName("pi") val pi: String,
        @SerialName("pl") val pl: List<Pl>,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    ) {
        @Serializable
        data class Pl(
            @SerialName("i") val i: Int,
            @SerialName("q") val q: Int,
        )
    }

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            pl = pl,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

