package smf.talkweb.medal.dao

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive
import packet.model.Credential
import service.model.User

@Serializable
data class UserDao(
    val userId: JsonPrimitive,
    val userNick: String? = null,
    val phone: String? = null,
    val password: String? = null,
    val token: String? = null,
    val pi: String? = null,
    val sk: String? = null,
    val ui: String? = null,
) {
    fun toUser() = User(
        userId = userId,
        userNick = userNick,
        phone = phone,
        password = password,
        token = token,
        _credential = if (pi == null || sk == null || ui == null) null else Credential(personalId = pi, securityKey = sk, userId = ui)
    )
}

fun User.toUserDao() = UserDao(
    userId = userId,
    userNick = userNick,
    phone = phone,
    password = password,
    token = token,
    pi = _credential?.personalId,
    sk = _credential?.securityKey,
    ui = _credential?.userId,
)