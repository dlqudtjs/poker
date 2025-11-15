package mong.poker.application.domain.room.gameroom.usecase

import mong.poker.application.domain.room.RoomManager
import mong.poker.application.global.support.usecase.UseCase
import mong.poker.core.domain.room.Room
import mong.poker.core.domain.room.command.CreateGameRoomCommand
import mong.poker.core.domain.user.UserInfo
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

@Component
class CreateGameRoomUseCase(
    private val roomManager: RoomManager,
) : UseCase<CreateGameRoomUseCase.Request, CreateGameRoomUseCase.Response> {

    override fun execute(
        request: Request,
        executedAt: LocalDateTime,
    ): Response {
        val gameRoomAccess = request.toGameRoomAccess()

        val command = request.toCreateGameRoomCommand(gameRoomAccess)

        val roomId = roomManager.createGameRoom(command)

        return Response(roomId = roomId)
    }

    data class Request(
        val userInfo: UserInfo,
        val roomName: String,
        val password: String?,
        val maxCapacity: Int,
        val totalRounds: Int,
        val bbAmount: Int,
        val sbAmount: Int,
    )

    data class Response(
        val roomId: UUID,
    )

    private fun Request.toGameRoomAccess(): Room.GameRoomAccess {
        return if (this.password.isNullOrEmpty()) {
            Room.GameRoomAccess.Public
        } else {
            Room.GameRoomAccess.Private(password = this.password)
        }
    }

    private fun Request.toCreateGameRoomCommand(access: Room.GameRoomAccess): CreateGameRoomCommand {
        return CreateGameRoomCommand(
            roomName = this.roomName,
            roomAccess = access,
            maxCapacity = this.maxCapacity,
            bbAmount = this.bbAmount,
            sbAmount = this.sbAmount,
            totalRounds = this.totalRounds,
            userInfo = this.userInfo,
        )
    }
}
