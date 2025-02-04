package packet.packets

import JsonFeature
import by
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import packet.Crypto
import packet.Packet
import service.model.User
import packet.PacketIdentifier
/**
 * 无尽挑战通关奖励
 *
 * @property upnl [TODO]
 * @property prpl [TODO]
 * @property lc 小推车存在状态
 * @property eb 持有的能量豆数量
 * @property ripl [TODO]
 * @property ls 关卡分数
 *
 * @see V322.Data
 */
@PacketIdentifier("V322")
class V322(override val user: User) : Packet() {
    override val identifier: String = "V322"

    /**
     * upnl
     *
     * ```
     * upnl = listOf(Data.Acd.Upnl(111022, 2))
     * ```
     */
    var upnl: List<Data.Acd.Upnl> = listOf()

    /**
     * prpl
     *
     * ```
     * prpl = listOf(Data.Pr.Pl(111022, 5)...)
     * ```
     */
    var prpl: List<Data.Pr.Pl> = listOf()

    /**
     * 小推车存在状态, 1 ~ 5 列不存在则为 0, 存在为 1
     *
     * ```
     * lc = listOf(1, 1, 1, 1, 1)
     * ```
     */
    var lc: List<Int> = listOf(1, 1, 1, 1, 1)

    /**
     * 持有的能量豆数量
     *
     * ```
     * eb = 0
     * ```
     */
    var eb: Int = 0

    /**
     * ripl
     *
     * ```
     * ripl = listOf(Data.Ri.Pl(111035, 8)...)
     * ```
     */
    var ripl: List<Data.Ri.Pl> = listOf()

    /**
     * 关卡分数
     *
     * ```
     * ls = 1750
     * ```
     */
    var ls: Int = 1750

    @Serializable
    data class Data(
        @SerialName("acd") val acd: Acd,
        @SerialName("fr") val fr: String = "1",
        @SerialName("pi") val pi: String = "662028550",
        @SerialName("pr") val pr: Pr,
        @SerialName("ri") val ri: Ri,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
        @SerialName("w") val w: String = "4",
    ) {
        @Serializable
        data class Acd(
            @SerialName("g") val g: Int = 0, // 账号钻石数量
            @SerialName("ubn") val ubn: Int = 0,
            @SerialName("uebn") val uebn: Int = 0,
            @SerialName("upnl") val upnl: List<Upnl>,
        ) {
            @Serializable
            data class Upnl(
                @SerialName("id") val id: Int,
                @SerialName("n") val n: Int,
            )
        }

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

        @Serializable
        data class Ri(
            @SerialName("alt") val alt: Int = 15,
            @SerialName("amt") val amt: Int = 15,
            @SerialName("bn") val bn: Int = 1,
            @SerialName("bu") val bu: Int = 0,
            @SerialName("cil") val cil: JsonArray = JsonArray(emptyList()),
            @SerialName("dm") val dm: String, // pl 通过 Gzip 加密后再进行 MD5 计算
            @SerialName("ds") val ds: Int = 0,
            @SerialName("eb") val eb: Int,
            @SerialName("eub") val eub: Int = 0,
            @SerialName("jc") val jc: Int = 0,
            @SerialName("jl") val jl: Int = 0,
            @SerialName("l") val l: Int = 0,
            @SerialName("lc") val lc: List<Int>,
            @SerialName("ls") val ls: Int,
            @SerialName("lwml") val lwml: Int = 0,
            @SerialName("m") val m: Int = 20,
            @SerialName("ml") val ml: Int = 0,
            @SerialName("on") val on: String = "62af8cd4b2b24360b47b496794625b3d", // 未知的 MD5 校验
            @SerialName("par") val par: Int = 336,
            @SerialName("pas") val pas: Int = 500,
            @SerialName("pl") val pl: String,
        ) {
            @Serializable
            data class Pl(
                @SerialName("id") val id: Int,
                @SerialName("n") val n: Int,
            )
        }
    }

    override fun build(): Pair<String, JsonObject> {
        val pl = Crypto.Gzip.encrypt(Json.encodeToString(ripl))
        return identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
            Data(
                acd = Data.Acd(upnl = upnl),
                pr = Data.Pr(pl = prpl),
                ri = Data.Ri(
                    pl = pl,
                    lc = lc,
                    eb = eb,
                    ls = ls,
                    dm = Crypto.getMD5(pl)
                ),
                pi = user.credential.personalId,
                sk = user.credential.securityKey,
                ui = user.credential.userId,
            )
        ).jsonObject
    }
}

