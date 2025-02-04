package packet.packets

import JsonFeature
import by
import config.AndroidConfig
import config.IOSConfig
import kotlinx.coroutines.runBlocking
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
import service.login
import service.model.User

/**
 * 获取用户登录凭据 [packet.model.Credential]
 *
 * Android Properties
 * @property oi 原始用户 id
 * @property t token 校验
 * @property cv 当前版本
 * @property li 绑定的 AndroidId MD5 校验
 * @property r r 校验
 * @property s s MD5 校验
 *
 * IOS Properties
 *
 */
@PacketIdentifier("V202")
class V202(override val user: User) : Packet() {
    override val identifier: String = "V202"

    // Android Properties
    /**
     * 原始用户 id
     *
     * ```
     * oi = "109208X12345678"
     * ```
     */
    var oi: String = "${config.channel.appId}${config.channel.channelId}X${user.userId.content}"

    /**
     * token 校验
     *
     * ```
     * t = runBlocking {
     *     login(
     *         phoneOrUserId = user.phone ?: user.userId.content,
     *         password = user.password!!
     *     ).token!!
     * }
     * ```
     */
    var t: String = "${user.token}"

    /**
     * 当前版本
     *
     * ```
     * cv = "x.x.x"
     * ```
     */
    var cv: String = config.channel.version

    /**
     * 绑定的 AndroidId MD5 校验
     *
     * ```
     * li = "84b8aa5c65c0d32b4f8ac2f5f0c0592f"
     * ```
     */
    var li: String = "84b8aa5c65c0d32b4f8ac2f5f0c0592f"

    /**
     * r 校验
     *
     * ```
     * r = "1600340008"
     * ```
     */
    var r: String = "1600340008"

    /**
     * s MD5 校验
     *
     * ```
     * s = "0e9788be5612edcaa9d03349f3cdf707"
     * ```
     */
    var s: String = "0e9788be5612edcaa9d03349f3cdf707"

    // IOS Properties
    /**
     * sk User Security Key
     */
    var sk: String = ""

    /**
     * ui UserId
     */
    var ui: String = ""

    @Serializable
    data class AndroidData(
        @SerialName("ci") val ci: String = "93",
        @SerialName("cv") val cv: String,
        @SerialName("di") val di: String = "",
        @SerialName("head") val head: Head = Head(),
        @SerialName("li") val li: String,
        @SerialName("oi") val oi: String,
        @SerialName("pi") val pi: String = "",
        @SerialName("r") val r: String,
        @SerialName("s") val s: String,
        @SerialName("t") val t: String,
        @SerialName("ui") val ui: String = "",
    ) {
        @Serializable
        data class Head(
            @SerialName("appId") val appId: String = "${config.channel.appId}",
            @SerialName("appVersion") val appVersion: String = "1.0",
            @SerialName("channelId") val channelId: String = "${config.channel.channelId}",
            @SerialName("channelSdkVersion") val channelSdkVersion: String = "dj2.0-2.0.0",
            @SerialName("talkwebSdkVersion") val talkwebSdkVersion: String = "3.0.0",
        )
    }

    @Serializable
    data class IOSData(
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to
        when (config) {
            AndroidConfig -> Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(AndroidData(
                oi = oi,
                t = if (user.password.isNullOrEmpty()) user.token!! else runBlocking {
                    login(
                        phoneOrUserId = user.phone ?: user.userId.content,
                        passwordMD5 = Crypto.getMD5(user.password!!)
                    ).token!!
                },
                cv = cv,
                li = li,
                r = r,
                s = s,
            )).jsonObject
            IOSConfig -> Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(IOSData(
                sk = sk,
                ui = ui,
            )).jsonObject
            else -> {
                throw NotImplementedError("Unsupported config: $config")
            }
        }
}