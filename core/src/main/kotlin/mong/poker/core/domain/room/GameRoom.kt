package mong.poker.core.domain.room

import mong.poker.core.domain.room.command.CreateGameRoomCommand
import mong.poker.core.domain.room.command.UpdateGameRoomCommand
import mong.poker.core.domain.room.enums.GameState
import java.util.*
import java.util.UUID.randomUUID

class GameRoom(
    val id: UUID,
    private var roomName: String,
    private var roomAccess: GameRoomAccess, // 게임방 공개 여부
    private var gameRoomStatus: GameRoomStatus, // 게임방 상태
    private var players: MutableList<UUID> = mutableListOf(), // 게임방에 참여한 플레이어 목록
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

    fun getRoomName(): String {
        return roomName
    }

    fun getRoomAccess(): GameRoomAccess {
        return roomAccess
    }

    fun getGameRoomStatus(): GameRoomStatus {
        return gameRoomStatus
    }

    sealed class GameRoomAccess {
        fun isPrivate(): Boolean {
            return this is Private
        }

        object Public : GameRoomAccess()
        data class Private(val password: String) : GameRoomAccess()
    }

    data class GameRoomStatus(
        private var bbAmount: Int,
        private var sbAmount: Int,
        private var maxPlayerCount: Int,
        private var gameState: GameState = GameState.WAITING,
        private var totalRounds: Int = 0, // 총 진행된 라운드 수
        private var currentRound: GameRound? = null  // 현재 진행 중인 라운드
    ) {
        companion object {
            fun create(
                bbAmount: Int,
                sbAmount: Int,
                maxPlayerCount: Int
            ) = GameRoomStatus(
                bbAmount = bbAmount,
                sbAmount = sbAmount,
                maxPlayerCount = maxPlayerCount
            )
        }

        // 게임방 상태 업데이트
        fun roomUpdate(
            bbAmount: Int,
            sbAmount: Int,
            maxPlayerCount: Int
        ) = GameRoomStatus(
            bbAmount = bbAmount,
            sbAmount = sbAmount,
            maxPlayerCount = maxPlayerCount,
        )

        fun getBbAmount(): Int {
            return bbAmount
        }

        fun getSbAmount(): Int {
            return sbAmount
        }

        fun getMaxPlayerCount(): Int {
            return maxPlayerCount
        }
    }
}
