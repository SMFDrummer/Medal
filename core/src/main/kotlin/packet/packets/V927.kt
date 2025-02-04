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
 * 追击通关
 *
 * @property s 追击分数
 * @property pl 植物列表
 */
@PacketIdentifier("V927")
class V927(override val user: User) : Packet() {
    override val identifier: String = "V927"

    /**
     * 追击分数
     *
     * ```
     * s = "36000"
     * ```
     */
    var s: String = "36000"

    /**
     * 植物列表, 有默认值, 但不适合所有账号
     *
     * ```
     * pl = listOf(Data.Pr.Pl(1045, 1), ...)
     * ```
     */
    var pl: List<Data.Pr.Pl> = listOf(
        Data.Pr.Pl(1045, 1),
        Data.Pr.Pl(1008, 1),
        Data.Pr.Pl(111064, 1),
        Data.Pr.Pl(111016, 1),
        Data.Pr.Pl(111019, 1),
        Data.Pr.Pl(200058, 1),
        Data.Pr.Pl(200034, 1)
    )

    @Serializable
    data class Data(
        @SerialName("fr") val fr: Fr,
        @SerialName("g") val g: String = "1",
        @SerialName("on") val on: String = "a2636e2574774169bee2ef522a5ffd5c", // 未知 MD5 校验
        @SerialName("pi") val pi: String,
        @SerialName("pr") val pr: Pr,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    ) {
        @Serializable
        data class Fr(
            @SerialName("b") val b: String = "1.000000",
            @SerialName("g") val g: String = "3",
            @SerialName("l") val l: String = "1",
            @SerialName("r") val r: String = "1",
            @SerialName("s") val s: String,
            @SerialName("t") val t: String = "1",
        )

        @Serializable
        data class Pr(
            @SerialName("pl") val pl: List<Pl>,
        ) {
            @Serializable
            data class Pl(
                @SerialName("i") val i: Int,
                @SerialName("q") val q: Int,
            )
        }
    }

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            fr = Data.Fr(s = s),
            pi = user.credential.personalId,
            pr = Data.Pr(pl = pl),
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

