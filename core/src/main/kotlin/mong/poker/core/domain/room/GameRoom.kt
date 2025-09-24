package mong.poker.core.domain.room

import mong.poker.core.domain.room.command.CreateGameRoomCommand
import java.util.*
import java.util.UUID.randomUUID

class GameRoom(
    val id: UUID,
    var roomName: String,
    val roomAccess: GameRoomAccess, // 게임방 공개 여부
    var gameRoomStatus: GameRoomStatus, // 게임방 상태
) {
    companion object {
        fun create(command: CreateGameRoomCommand): GameRoom {
            return GameRoom(
                id = randomUUID(),
                roomName = command.roomName,
                roomAccess = command.roomAccess,
                gameRoomStatus = GameRoomStatus.create(
                    bbAmount = command.bbAmount,
                    sbAmount = command.sbAmount,
                    maxPlayerCount = command.maxPlayerCount
                )
            )
        }
    }

    sealed class GameRoomAccess {
        object Public : GameRoomAccess()
        data class Private(val password: String) : GameRoomAccess()
    }
}
