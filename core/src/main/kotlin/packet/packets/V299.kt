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
import parseObject
import service.model.User
import packet.PacketIdentifier
/**
 * 碎片挑战
 *
 * @property p 挑战状态
 * @property ad 广告翻倍
 * @property ii 挑战 id
 *
 * @param ol 奖励列表
 */
@PacketIdentifier("V299")
class V299(override val user: User) : Packet() {
    override val identifier: String = "V299"

    /**
     * 挑战状态
     *
     * ```
     * p = "1" // 挑战
     * p = "2" // 增加次数
     * ```
     */
    var p: String = "1"

    /**
     * 广告翻倍
     *
     * ```
     * ad = "0" // 不翻倍
     * ad = "1" // 广告翻倍
     * ```
     */
    var ad: String = "0"

    /**
     * 挑战 id
     *
     * ```
     * ii = "10502" // 巨人危机
     * ii = "10503" // 邪恶入侵
     * ```
     */
    var ii: String = "10502"

    /**
     * 奖励列表, p 为 1 时拥有列表, p 为 2 时为 null
     *
     * ```
     * {
     *     "1193": 10,
     *     "1123": 10,
     *     "1159": 10,
     *     "1161": 10,
     *     "111104": 10,
     *     "111123": 10,
     *     "1114": 10,
     *     "111125": 10,
     *     "1108": 10,
     *     "1140": 10,
     *     "1160": 10,
     *     "1180": 10,
     *     "111118": 10,
     *     "111138": 10,
     *     "111126": 10,
     *     "111113": 10,
     *     "1175": 10,
     *     "1168": 10,
     *     "1127": 10,
     *     "1119": 10,
     *     "1188": 10,
     *     "111128": 10,
     *     "1137": 10,
     *     "1185": 10,
     *     "1176": 10,
     *     "1107": 10,
     *     "1120": 10,
     *     "1122": 10,
     *     "1124": 10,
     *     "1128": 10,
     *     "1129": 10,
     *     "1130": 10,
     *     "1195": 10,
     *     "1182": 10,
     *     "1184": 10,
     *     "1192": 10,
     *     "1101": 10,
     *     "1103": 10,
     *     "1104": 10,
     *     "1105": 10,
     *     "1106": 10,
     *     "1189": 10,
     *     "111137": 10,
     *     "1135": 10,
     *     "1149": 10,
     *     "111110": 10,
     *     "1111": 10
     * }
     * ```
     */
    val ol: JsonObject? = if (p == "1") Json.parseObject(
        """
        {"1193": 10, "1123": 10, "1159": 10, "1161": 10, "111104": 10, "111123": 10,
        "1114": 10, "111125": 10, "1108": 10, "1140": 10, "1160": 10, "1180": 10,
        "111118": 10, "111138": 10, "111126": 10, "111113": 10, "1175": 10,
        "1168": 10, "1127": 10, "1119": 10, "1188": 10, "111128": 10, "1137": 10,
        "1185": 10, "1176": 10, "1107": 10, "1120": 10, "1122": 10, "1124": 10,
        "1128": 10, "1129": 10, "1130": 10, "1195": 10, "1182": 10, "1184": 10,
        "1192": 10, "1101": 10, "1103": 10, "1104": 10, "1105": 10, "1106": 10,
        "1189": 10, "111137": 10, "1135": 10, "1149": 10, "111110": 10, "1111": 10}
    """.trimIndent()
    ) else null


    @Serializable
    data class Data(
        @SerialName("p") val p: String,
        @SerialName("ad") val ad: String,
        @SerialName("ii") val ii: String,
        @SerialName("ol") val ol: JsonObject?,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            p = p,
            ad = ad,
            ii = ii,
            ol = ol,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}