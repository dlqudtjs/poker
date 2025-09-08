package mong.poker.application.domain.user.usecase

import mong.poker.application.domain.auth.service.PasswordAccountService
import mong.poker.application.domain.user.service.UserService
import mong.poker.application.global.support.exception.CustomException
import mong.poker.application.global.support.exception.ErrorType
import mong.poker.application.global.support.usecase.UseCase
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Component
class SignUnUpUseCase(
    private val userService: UserService,
    private val passwordAccountService: PasswordAccountService
) : UseCase<SignUnUpUseCase.Request, SignUnUpUseCase.Response> {

    @Transactional(propagation = Propagation.REQUIRED)
    override fun execute(
        request: Request,
        executedAt: LocalDateTime,
    ): Response {
        if (userService.existByNickname(request.nickname)) {
            throw CustomException(ErrorType.DUPLICATED_NICKNAME)
        }

        val user = userService.create(
            nickname = request.nickname,
            executedAt = executedAt,
        )

        passwordAccountService.create(
            user = user,
            accountId = request.accountId,
            password = request.password,
            executedAt = executedAt,
        )

        return Response(id = user.id)
    }

    data class Request(
        val accountId: String,
        val password: String,
        val nickname: String,
    )

    data class Response(
        val id: UUID,
    )
}
