package mong.poker.application.domain.auth.service

import mong.poker.application.domain.auth.repository.PasswordAccountRepository
import mong.poker.core.domain.auth.domain.PasswordAccount
import mong.poker.core.domain.user.domain.User
import mong.poker.lib.encrypt.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class PasswordAccountService(
    private val passwordAccountRepository: PasswordAccountRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    @Transactional(propagation = Propagation.REQUIRED)
    fun create(
        user: User,
        accountId: String,
        password: String,
        executedAt: LocalDateTime,
    ) {
        passwordAccountRepository.save(
            PasswordAccount.create(
                user = user,
                accountId = accountId,
                password = passwordEncoder.encode(password),
                createdAt = executedAt,
            )
        )
    }
}
