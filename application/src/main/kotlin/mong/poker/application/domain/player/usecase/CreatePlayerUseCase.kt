package mong.poker.application.domain.player.usecase

import mong.poker.application.domain.player.service.PlayerService
import mong.poker.application.global.support.usecase.UseCase
import mong.poker.core.domain.player.Player
import mong.poker.core.domain.user.UserInfo
import org.springframework.stereotype.Component
import java.util.*

@Component
class CreatePlayerUseCase(
    private val playerService: PlayerService,
) : UseCase<CreatePlayerUseCase.Request, CreatePlayerUseCase.Response> {

    override fun execute(
        request: Request,
        executedAt: java.time.LocalDateTime,
    ): Response {
        playerService.createPlayer(
            userInfo = request.userInfo,
            executedAt = executedAt,
        )

        return Response(userId = request.userInfo.id)
    }

    data class Request(
        val location: Player.UserLocation,
        val userInfo: UserInfo,
    )

    data class Response(
        val userId: UUID,
    )
}
