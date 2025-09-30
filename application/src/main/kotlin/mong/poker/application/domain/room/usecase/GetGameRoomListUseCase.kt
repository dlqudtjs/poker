package mong.poker.application.domain.room.usecase

import mong.poker.application.domain.room.service.GameRoomService
import mong.poker.application.global.support.usecase.UseCase
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

@Component
class GetGameRoomListUseCase(
    private val gameRoomService: GameRoomService,
) : UseCase<Unit, GetGameRoomListUseCase.Response> {

    override fun execute(
        request: Unit,
        executedAt: LocalDateTime,
    ): Response {
        val rooms = gameRoomService.getAllRooms().map { room ->
            RoomInfo(
                roomId = room.id,
                roomName = room.getRoomName(),
                maxUserCount = room.getGameRoomStatus().getMaxPlayerCount(),
                isPrivate = room.getRoomAccess().isPrivate(),
                bbAmount = room.getGameRoomStatus().getBbAmount(),
                sbAmount = room.getGameRoomStatus().getSbAmount()
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
        val maxUserCount: Int,
        val isPrivate: Boolean,
        val bbAmount: Int,
        val sbAmount: Int,
    )
}
