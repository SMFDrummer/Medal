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
 * 无尽选取 80 植物上传
 *
 * @property pl 植物列表
 */
@PacketIdentifier("V908")
class V908(override val user: User) : Packet() {
    override val identifier: String = "V908"

    /**
     * 植物列表, 该值含有一个默认值, 但可能不适配所有账号
     *
     * ```
     * pl = listOf(111035, 200006, 111075, ...)
     * ```
     */
    var pl: List<Int> = listOf(
        111035,
        200006,
        111075,
        111079,
        111081,
        111082,
        111085,
        111088,
        111090,
        200000,
        200002,
        200004,
        200005,
        111074,
        200008,
        200009,
        200010,
        200012,
        200015,
        200019,
        200020,
        200021,
        200022,
        200023,
        200024,
        111046,
        111019,
        111021,
        111022,
        111029,
        111030,
        111031,
        111032,
        111033,
        111042,
        111043,
        111044,
        200025,
        111047,
        111048,
        111049,
        111050,
        111055,
        111060,
        111061,
        111067,
        111070,
        111071,
        111072,
        200070,
        200058,
        200059,
        200061,
        200062,
        200063,
        200064,
        200065,
        200066,
        200067,
        200068,
        200069,
        200057,
        200071,
        200072,
        200073,
        200074,
        200077,
        200078,
        200079,
        200080,
        200082,
        200083,
        200084,
        200044,
        200026,
        200028,
        200029,
        200031,
        200032,
        200033
    )

    @Serializable
    data class Data(
        @SerialName("pi") val pi: String,
        @SerialName("pl") val pl: List<Int>,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            pl = pl,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

