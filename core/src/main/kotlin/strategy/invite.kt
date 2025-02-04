package strategy

import dsl.buildStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.put
import primitive
import service.model.User
import to
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.exists

fun androidInvite(inviteCode: String) = buildStrategy {
    version = 1
    description = "Android - 刷邀请码"

    packet(
        i = "V316"
    )

    packet(
        i = "V206",
        cycle = 2,
        onFailure = primitive { false }
    )

    packet(
        i = "V900",
        t = linkedMapOf(
            "pl" to buildJsonArray {
                addJsonObject {
                    put("i", 1001)
                    put("q", 1)
                }
                addJsonObject {
                    put("i", 1002)
                    put("q", 1)
                }
                addJsonObject {
                    put("i", 1003)
                    put("q", 1)
                }
                addJsonObject {
                    put("i", 1004)
                    put("q", 1)
                }
            }
        ),
        onFailure = primitive { false }
    )

    packet(
        i = "V303",
        t = linkedMapOf(
            "al" to buildJsonArray {
                addJsonObject {
                    put("abi", 0)
                    put("config_version", 1)
                    put("id", 10868)
                    put("type", 1)
                }
            }
        ),
        retry = 1,
        onFailure = primitive { false }
    )

    packet(
        i = "V876",
        t = linkedMapOf(
            "code" to primitive { inviteCode },
            "star" to primitive { "80" }
        ),
        onFailure = primitive { false },
        onSuccess = primitive { true }
    )
}

fun iosInvite(inviteCode: String) = buildStrategy {
    version = 1
    description = "IOS - 刷邀请码"

    packet(
        i = "V216"
    )

    packet(
        i = "V205",
        cycle = 2,
        onFailure = primitive { false }
    )

    packet(
        i = "V900",
        t = linkedMapOf(
            "pl" to buildJsonArray {
                addJsonObject {
                    put("i", 1001)
                    put("q", 1)
                }
                addJsonObject {
                    put("i", 1002)
                    put("q", 1)
                }
                addJsonObject {
                    put("i", 1003)
                    put("q", 1)
                }
                addJsonObject {
                    put("i", 1004)
                    put("q", 1)
                }
            }
        ),
        onFailure = primitive { false }
    )

    packet(
        i = "V303",
        t = linkedMapOf(
            "al" to buildJsonArray {
                addJsonObject {
                    put("abi", 0)
                    put("config_version", 1)
                    put("id", 10868)
                    put("type", 1)
                }
            }
        ),
        retry = 1,
        onFailure = primitive { false }
    )

    packet(
        i = "V876",
        t = linkedMapOf(
            "code" to primitive { inviteCode },
            "star" to primitive { "80" }
        ),
        onFailure = primitive { false },
        onSuccess = primitive { true }
    )
}