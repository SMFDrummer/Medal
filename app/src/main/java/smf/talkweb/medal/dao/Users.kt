package smf.talkweb.medal.dao

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import service.model.User

@Serializable
data class Users(
    @SerialName("Users") val users: List<User>
)