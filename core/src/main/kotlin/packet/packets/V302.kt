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
 * 困难宝箱
 *
 * @property nfc [TODO]
 * @property id 物品 id
 * @property q 物品数量
 * @property fName 世界名
 * @property fId 关卡 id
 */
@PacketIdentifier("V302")
class V302(override val user: User) : Packet() {
    override val identifier: String = "V302"

    /**
     * [TODO]
     *
     * ```
     * nfc = "1"
     * ```
     */
    var nfc: String = "1"

    /**
     * 物品 id
     *
     * ```
     * i = listof(1111, 2222)
     * ```
     */
    var i: List<Int> = listOf(1111, 2222)

    /**
     * 物品数量
     *
     * ```
     * q = 1
     * ```
     */
    var q: Int = 1

    var uk: Int = user.uk

    /**
     * 世界名, 默认使用 steam （蒸汽世界）, 恢复正常响应请使用自定义
     */
    var fName: String = "steam"

    /**
     * 关卡 id, 默认为同步 uk, 恢复正常响应请使用自定义
     */
    var fId: Int = uk

    @Serializable
    data class Data(
        @SerialName("nfc") val nfc: String,
        @SerialName("o") val o: List<O>,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
        @SerialName("uk") val uk: String,
    ) {
        @Serializable
        data class O(
            @SerialName("f") val f: String,
            @SerialName("i") val i: Int,
            @SerialName("q") val q: Int,
        )
    }

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            nfc = nfc,
            o = i.map { Data.O(i = it, q = q, f = "$fName${fId}_hard_level_reward") },
            uk = "$uk",
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

