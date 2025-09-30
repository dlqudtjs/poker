package mong.poker.application.domain.user.service

import mong.poker.application.domain.user.repository.UserRepository
import mong.poker.core.domain.user.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository
) {
    companion object {
        private val logger = org.slf4j.LoggerFactory.getLogger(UserService::class.java)
    }

    fun existByNickname(nickname: String): Boolean {
        return userRepository.existsUserByNickname(nickname)
    }

    @Transactional(propagation = Propagation.REQUIRED)
    fun create(
        nickname: String,
        executedAt: LocalDateTime,
    ): User {
        val user = userRepository.save(
            User.create(
                nickname = nickname,
                createdAt = executedAt,
            )
        )

        logger.info("유저 생성 userId: ${user.getId()}, nickname: ${user.getNickname()}")
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    fun getById(id: UUID): User? {
        return userRepository.findByIdOrNull(id)
    }
}
