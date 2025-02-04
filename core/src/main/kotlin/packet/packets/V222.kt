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
 * 无尽商店无尽币兑换界面打开
 *
 * ```
 * st = 1; rs = 0; rv = "5"; t = "13"
 * ```
 * 无尽币商店广告刷新奖励
 *
 * ```
 * st = 1; rs = 2; rv = "5"; t = "13"
 * ```
 *
 * 无尽币商店无尽币刷新奖励
 *
 * ```
 * st = 1; rs = 1; rv = "5"; t = "13"
 * ```
 *
 * 无尽购买当前世界无尽植物
 *
 * ```
 * st = 2; rs = 0; rv = "5"; t = "13"
 * ```
 *
 * 无尽获取排行榜
 *
 * ```
 * of = 0; rv = "5"; t = "12"
 * ```
 */
@PacketIdentifier("V222")
class V222(override val user: User) : Packet() {
    override val identifier: String = "V222"

    var st: Int? = null

    var rs: Int? = null

    var of: Int? = null

    var rv: String = "5"

    var t: String = ""

    @Serializable
    data class Data(
        @SerialName("p") val p: P,
        @SerialName("pi") val pi: String,
        @SerialName("rv") val rv: String,
        @SerialName("sk") val sk: String,
        @SerialName("t") val t: String,
        @SerialName("ui") val ui: String,
    ) {
        @Serializable
        data class P(
            @SerialName("rs") val rs: Int?,
            @SerialName("st") val st: Int?,
            @SerialName("of") val of: Int?,
        )
    }

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            p = Data.P(
                rs = rs,
                st = st,
                of = of,
            ),
            t = t,
            rv = rv,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}



