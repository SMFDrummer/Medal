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
 * 超 Z 对战胜利
 *
 * @property cp [TODO]
 * @property lfs [TODO]
 * @property pl 植物列表
 * @property r [TODO]
 */
@PacketIdentifier("V381")
class V381(override val user: User) : Packet() {
    override val identifier: String = "V381"

    /**
     * [TODO]
     *
     * ```
     * cp = "1770"
     * ```
     */
    var cp: String = "1770"

    /**
     * [TODO]
     *
     * ```
     * lfs = "81360"
     * ```
     */
    var lfs: String = "81360"

    /**
     * 植物列表
     *
     * ```
     * pl = listOf(Data.Pr.Pl(200080, 5)...)
     * ```
     */
    var pl: List<Data.Pr.Pl> = listOf()

    /**
     * [TODO]
     *
     * ```
     * r = TODO()
     * ```
     */
    var r: String = ""

    @Serializable
    data class Data(
        @SerialName("cp") val cp: String,
        @SerialName("lfs") val lfs: String,
        @SerialName("pi") val pi: String,
        @SerialName("pr") val pr: Pr,
        @SerialName("r") val r: String,
        @SerialName("s") val s: String = "1",
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    ) {
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
            cp = cp,
            lfs = lfs,
            pr = Data.Pr(pl),
            r = r,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId
        )
    ).jsonObject
}

