package strategy

import dsl.buildStrategy
import primitive

fun androidCredential() = buildStrategy {
    version = 1
    description = "Android - 获取用户凭据"

    packet(
        i = "V202",
        r = 0,
        t = linkedMapOf(),
        cycle = 1,
        retry = 4,
        onSuccess = primitive { true }
    )
}

fun iosCredentialInit() = buildStrategy {
    version = 1
    description = "IOS - 获取用户凭据 - 初始化"

    packet(
        i = "V201",
        r = 0,
        t = linkedMapOf(),
        cycle = 1,
        retry = 4,
        onSuccess = primitive { true }
    )
}

fun iosCredentialNonRandom(sk: String, ui: String) = buildStrategy {
    version = 1
    description = "IOS - 获取用户凭据 - 非随机用户"

    packet(
        i = "V202",
        r = 0,
        t = linkedMapOf(
            "sk" to primitive { sk },
            "ui" to primitive { ui }
        ),
        cycle = 1,
        retry = 4,
        onSuccess = primitive { true }
    )
}

fun iosCredentialRandom(sk: String, ui: String) = buildStrategy {
    version = 1
    description = "IOS - 获取用户凭据 - 随机用户"

    packet(
        i = "V203",
        r = 0,
        t = linkedMapOf(
            "sk" to primitive { sk },
            "ui" to primitive { ui }
        ),
        cycle = 1,
        retry = 4,
        onSuccess = primitive { true }
    )
}