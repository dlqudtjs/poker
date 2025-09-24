package mong.poker.application.domain.user.usecase

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
class GetMyProfileUseCase(
    private val userService: UserService,
) : UseCase<GetMyProfileUseCase.Request, GetMyProfileUseCase.Response> {

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    override fun execute(
        request: Request,
        executedAt: LocalDateTime,
    ): Response {
        val user = userService.getById(request.userId)
            ?: throw CustomException(ErrorType.USER_NOT_FOUND)

        return Response(
            nickname = user.getNickname()
        )
    }

    data class Request(
        val userId: UUID,
    )

    data class Response(
        val nickname: String,
    )
}
