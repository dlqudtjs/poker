package mong.poker.application.domain.room.gameroom.usecase

import mong.poker.application.domain.room.RoomManager
import mong.poker.application.global.support.usecase.UseCase
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

@Component
class GetGameRoomListUseCase(
    private val roomManager: RoomManager,
) : UseCase<Unit, GetGameRoomListUseCase.Response> {

    override fun execute(
        request: Unit,
        executedAt: LocalDateTime,
    ): Response {
        val rooms = roomManager.getAllGameRoom().map { room ->
            RoomInfo(
                roomId = room.id,
                roomName = room.name,
                maxCapacity = room.maxCapacity,
                isPrivate = room.roomAccess.isPrivate(),
                bbAmount = room.gameRoomStatus.getSbAmount(),
                sbAmount = room.gameRoomStatus.getBbAmount(),
                totalRounds = room.gameRoomStatus.getTotalRounds(),
                currentPlayerCount = room.players.size,
            )
        }

        return Response(rooms)
    }

    data class Response(
        val rooms: List<RoomInfo>
    )

    data class RoomInfo(
        val roomId: UUID,
        val roomName: String,
        val maxCapacity: Int,
        val isPrivate: Boolean,
        val bbAmount: Int,
        val sbAmount: Int,
        val totalRounds: Int,
        val currentPlayerCount: Int,
    )
}
