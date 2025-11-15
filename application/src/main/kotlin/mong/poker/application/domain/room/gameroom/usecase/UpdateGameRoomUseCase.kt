package mong.poker.application.domain.room.gameroom.usecase

import mong.poker.application.domain.room.RoomManager
import mong.poker.application.global.support.usecase.UseCase
import mong.poker.core.domain.room.Room
import mong.poker.core.domain.room.command.UpdateGameRoomCommand
import mong.poker.core.domain.user.UserInfo
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

@Component
class UpdateGameRoomUseCase(
    private val roomManager: RoomManager,
) : UseCase<UpdateGameRoomUseCase.Request, UpdateGameRoomUseCase.Response> {

    override fun execute(
        request: Request,
        executedAt: LocalDateTime,
    ): Response {
        val gameRoomAccess = request.toGameRoomAccess()

        val command = request.toCreateGameRoomCommand(gameRoomAccess)

        val roomId = roomManager.updateGameRoom(command)

        return Response(roomId = roomId)
    }

    data class Request(
        val roomId: UUID,
        val roomName: String,
        val password: String?,
        val maxCapacity: Int,
        val bbAmount: Int,
        val sbAmount: Int,
        val totalRounds: Int,
        val userInfo: UserInfo,
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

    private fun Request.toCreateGameRoomCommand(access: Room.GameRoomAccess): UpdateGameRoomCommand {
        return UpdateGameRoomCommand(
            roomId = this.roomId,
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
