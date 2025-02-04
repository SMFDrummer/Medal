package packet.packets

import JsonFeature
import by
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import manager.config
import packet.Crypto
import packet.Packet
import packet.PacketIdentifier
import service.model.User

/**
 * IOS 获取用户登录凭据 ui 与 sk
 *
 * @property cv 当前版本
 * @property di IOS di 校验，udid 的 MD5 字符串
 * @property r IOS r 校验，1000000000 到 2000000000 的一个随机整数
 * @property s IOS s 校验，为 [di]、[r] 与 B7108D8B5TABE 拼接字符串的 MD5 字符串
 */
@PacketIdentifier("V201")
class V201(override val user: User): Packet() {
    override val identifier: String = "V201"

    /**
     * 当前版本
     *
     * ```
     * cv = config.channel.version
     * ```
     */
    var cv = config.channel.version

    /**
     * IOS di 校验，udid 的 MD5 字符串
     *
     * ```
     * di = Crypto.getMD5(user.userId.content)
     * ```
     */
    var di = Crypto.getMD5(user.userId.content)

    /**
     * IOS r 校验，1000000000 到 2000000000 的一个随机整数
     *
     * ```
     * r = (1000000000..2000000000).random().toString()
     * ```
     */
    var r = (1000000000..2000000000).random().toString()

    /**
     * IOS s 校验，为 [di]、[r] 与 B7108D8B5TABE 拼接字符串的 MD5 字符串
     *
     * ```
     * s = Crypto.getMD5(di + r + "B7108D8B5TABE")
     * ```
     */
    var s = Crypto.getMD5(di + r + "B7108D8B5TABE")

    @Serializable
    data class Data(
        @SerialName("cv") val cv: String,
        @SerialName("di") val di: String,
        @SerialName("r") val r: String,
        @SerialName("s") val s: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            cv = cv,
            di = di,
            r = r,
            s = s
        )
    ).jsonObject
}