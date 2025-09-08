package mong.poker.application.domain.user.repository

import mong.poker.core.domain.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun existsUserByNickname(nickname: String): Boolean
}

