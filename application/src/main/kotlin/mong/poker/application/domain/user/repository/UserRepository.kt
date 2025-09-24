package mong.poker.application.domain.user.repository

import mong.poker.core.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, UUID> {
    fun existsUserByNickname(nickname: String): Boolean
}

