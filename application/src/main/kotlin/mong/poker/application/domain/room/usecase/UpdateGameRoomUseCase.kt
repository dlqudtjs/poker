package mong.poker.application.domain.room.usecase

import mong.poker.application.domain.room.service.GameRoomService
import mong.poker.application.global.support.usecase.UseCase
import mong.poker.core.domain.room.GameRoom
import mong.poker.core.domain.room.command.UpdateGameRoomCommand
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

@Component
class UpdateGameRoomUseCase(
    private val gameRoomService: GameRoomService,
) : UseCase<UpdateGameRoomUseCase.Request, UpdateGameRoomUseCase.Response> {

    override fun execute(
        request: Request,
        executedAt: LocalDateTime,
    ): Response {
        val gameRoomAccess = request.toGameRoomAccess()

        val command = request.toCreateGameRoomCommand(gameRoomAccess)

        val roomId = gameRoomService.updateRoom(command)

        return Response(roomId = roomId)
    }

    data class Request(
        val roomId: UUID,
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
        return if (this.password.isNullOrEmpty()) {
            GameRoom.GameRoomAccess.Public
        } else {
            GameRoom.GameRoomAccess.Private(password = this.password)
        }
    }

    private fun Request.toCreateGameRoomCommand(access: GameRoom.GameRoomAccess): UpdateGameRoomCommand {
        return UpdateGameRoomCommand(
            roomId = this.roomId,
            roomName = this.roomName,
            roomAccess = access,
            maxPlayerCount = this.maxUserCount,
            bbAmount = this.bbAmount,
            sbAmount = this.sbAmount,
        )
    }
}
