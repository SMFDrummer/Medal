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
 * 僵局逃脱通关
 *
 * @property li 关卡 id
 */
@PacketIdentifier("V1030")
class V1030(override val user: User) : Packet() {
    override val identifier: String = "V1030"

    /**
     * 关卡 id
     *
     * ```
     * li = "1" // 1 ~ 5
     * ```
     */
    var li: String = "1"

    @Serializable
    data class Data(
        @SerialName("ba") val ba: Ba = Ba(),
        @SerialName("li") val li: String,
        @SerialName("pi") val pi: String,
        @SerialName("rt") val rt: String = "2",
        @SerialName("sk") val sk: String,
        @SerialName("ss") val ss: String = "8760,5900",
        @SerialName("ui") val ui: String,
        @SerialName("wi") val wi: String = "1",
    ) {
        @Serializable
        data class Ba(
            @SerialName("d") val d: D = D(),
        ) {
            @Serializable
            data class D(
                @SerialName("plantlist") val plantList: List<List<String>> = listOf(
                    listOf(
                        "cherry_bomb",
                        "mangosteen",
                        "primalsunflower",
                        "cactus",
                        "primalpeashooter",
                        "primalwallnut",
                        "draftodil",
                        "convallariachemist"
                    ),
                    listOf(
                        "mulberry",
                        "birthsunflower",
                        "powerplant",
                        "gatlingpea",
                        "applemortar",
                        "slingpea",
                        "pokra",
                        "burdockbatter"
                    )
                ),
            )
        }
    }

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            li = li,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

