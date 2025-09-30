package mong.poker.core.domain.room

import mong.poker.core.domain.room.command.CreateGameRoomCommand
import mong.poker.core.domain.room.command.UpdateGameRoomCommand
import java.util.*
import java.util.UUID.randomUUID

class GameRoom(
    val id: UUID,
    private var roomName: String,
    private var roomAccess: GameRoomAccess, // 게임방 공개 여부
    private var gameRoomStatus: GameRoomStatus, // 게임방 상태
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

    fun update(command: UpdateGameRoomCommand) {
        this.roomName = command.roomName
        this.roomAccess = command.roomAccess
        this.gameRoomStatus = this.gameRoomStatus.roomUpdate(
            bbAmount = command.bbAmount,
            sbAmount = command.sbAmount,
            maxPlayerCount = command.maxPlayerCount
        )
    }

    sealed class GameRoomAccess {
        fun isPrivate(): Boolean {
            return this is Private
        }

        object Public : GameRoomAccess()
        data class Private(val password: String) : GameRoomAccess()
    }

    fun getRoomName(): String {
        return roomName
    }

    fun getRoomAccess(): GameRoomAccess {
        return roomAccess
    }

    fun getGameRoomStatus(): GameRoomStatus {
        return gameRoomStatus
    }
}
