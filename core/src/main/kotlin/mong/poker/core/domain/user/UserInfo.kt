package mong.poker.core.domain.user

import java.util.*

data class UserInfo(
    var id: UUID,
    var nickname: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserInfo) return false

        return id == other.id && nickname == other.nickname
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + nickname.hashCode()
        return result
    }
}
