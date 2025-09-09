package mong.poker.application.domain.user.usecase

import TokenManager
import mong.poker.application.domain.auth.service.PasswordAccountService
import mong.poker.application.global.support.exception.CustomException
import mong.poker.application.global.support.exception.ErrorType
import mong.poker.application.global.support.usecase.UseCase
import mong.poker.core.domain.user.domain.User
import mong.poker.lib.encrypt.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class SignInUseCase(
    private val passwordAccountService: PasswordAccountService,
    private val passwordEncoder: PasswordEncoder,
    private val tokenManager: TokenManager,
) : UseCase<SignInUseCase.Request, SignInUseCase.Response> {

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    override fun execute(
        request: Request,
        executedAt: LocalDateTime,
    ): Response {
        val account = passwordAccountService.getAccountByAccountId(request.accountId)
            ?: throw CustomException(ErrorType.INVALID_LOGIN_INFO)

        if (!passwordEncoder.matches(request.password, account.getPassword())) {
            throw CustomException(ErrorType.INVALID_LOGIN_INFO)
        }

        return Response(token = createAuthToken(account.getUser(), executedAt))
    }


    private fun createAuthToken(
        user: User,
        now: LocalDateTime,
    ): String =
        tokenManager.createToken(
            payload =
                mapOf(
                    "id" to user.getId()
                ),
            issuedAt = now,
        )

    data class Request(
        val accountId: String,
        val password: String,
    )

    data class Response(
        val token: String,
    )
}
