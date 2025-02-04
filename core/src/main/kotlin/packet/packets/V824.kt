package packet.packets

import JsonFeature
import by
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import packet.Packet
import service.model.User
import packet.PacketIdentifier
/**
 * 双人对决位置交互
 *
 * @suppress 自定义
 */
@PacketIdentifier("V824")
class V824(override val user: User) : Packet() {
    override val identifier: String = "V824"

    @Serializable
    data class Data(
        @SerialName("di") val di: Di = Di(),
        @SerialName("lf") val lf: String = "0",
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    ) {
        @Serializable
        data class Di(
            @SerialName("d") val d: D = D(),
        ) {
            @Serializable
            data class D(
                @SerialName("evs") val evs: List<Ev> = listOf(Ev()),
            ) {
                @Serializable
                data class Ev(
                    @SerialName("e") val e: Int = 8,
                    @SerialName("ev") val ev: JsonArray = JsonArray(emptyList()),
                    @SerialName("i") val i: Int = 0,
                    @SerialName("p") val p: P = P(),
                    @SerialName("s") val s: Int = 0,
                    @SerialName("t") val t: Int = -1,
                    @SerialName("ti") val ti: Double = 0.560000,
                    @SerialName("v") val v: Double = 0.0,
                ) {
                    @Serializable
                    data class P(
                        @SerialName("t") val t: Int = 0,
                        @SerialName("x") val x: Double = -1.000000,
                        @SerialName("y") val y: Double = -1.000000,
                    )
                }
            }
        }
    }

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

