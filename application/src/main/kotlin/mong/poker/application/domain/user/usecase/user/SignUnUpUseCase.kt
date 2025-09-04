package mong.poker.application.domain.user.usecase.user

import mong.poker.application.domain.user.repository.UserRepository
import mong.poker.application.global.support.usecase.UseCase
import mong.poker.core.domain.user.domain.User
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SignUnUpUseCase(
    private val userRepository: UserRepository,
) : UseCase<SignUnUpUseCase.Request, SignUnUpUseCase.Response> {

    override fun execute(
        request: Request,
        executedAt: LocalDateTime,
    ): Response {
        val user = userRepository.save(
            User.create(
                nickname = request.nickname,
                createdAt = executedAt,
            )
        )

        return Response(id = user.id.toString())
    }

    data class Request(
        val nickname: String,
    )

    data class Response(
        val id: String,
    )
}
