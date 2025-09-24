package mong.poker.application.domain.room.usecase

import mong.poker.application.domain.room.service.GameRoomService
import mong.poker.application.global.support.usecase.UseCase
import mong.poker.core.domain.room.GameRoom
import mong.poker.core.domain.room.command.CreateGameRoomCommand
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

@Component
class CreateGameRoomUseCase(
    private val gameRoomService: GameRoomService,
) : UseCase<CreateGameRoomUseCase.Request, CreateGameRoomUseCase.Response> {

    override fun execute(
        request: Request,
        executedAt: LocalDateTime,
    ): Response {
        val gameRoomAccess = request.toGameRoomAccess()

        val command = request.toCreateGameRoomCommand(gameRoomAccess)

        gameRoomService.createRoom(command)

        return Response(roomId = UUID.randomUUID())
    }

    data class Request(
        val hostUserId: UUID,
        val roomName: String,
        val password: String?,
        val maxUserCount: Int,
        val bbAmount: Int,
        val sbAmount: Int,
    )

    data class Response(
        val roomId: UUID,
    )

    private fun Request.toGameRoomAccess(): GameRoom.GameRoomAccess {
        return if (this.password.isNullOrBlank()) {
            GameRoom.GameRoomAccess.Public
        } else {
            GameRoom.GameRoomAccess.Private(password = this.password)
        }
    }

    private fun Request.toCreateGameRoomCommand(access: GameRoom.GameRoomAccess): CreateGameRoomCommand {
        return CreateGameRoomCommand(
            roomName = this.roomName,
            roomAccess = access,
            maxPlayerCount = this.maxUserCount,
            bbAmount = this.bbAmount,
            sbAmount = this.sbAmount,
            hostUserId = this.hostUserId,
        )
    }
}
