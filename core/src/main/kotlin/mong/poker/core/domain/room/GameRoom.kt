package mong.poker.core.domain.room

import mong.poker.core.domain.player.Player
import mong.poker.core.domain.room.command.CreateGameRoomCommand
import mong.poker.core.domain.room.command.UpdateGameRoomCommand
import mong.poker.core.domain.room.enums.GameState
import mong.poker.core.domain.user.UserInfo
import java.util.*
import java.util.UUID.randomUUID

data class GameRoom(
    override val id: UUID,
    override var name: String,
    override var roomAccess: GameRoomAccess,
    override var maxCapacity: Int,
    override val players: MutableSet<Player> = mutableSetOf(),
    var owner: UserInfo,
    val gameState: GameState = GameState.WAITING,
    val gameRoomStatus: GameRoomStatus,
) : Room(id, name, roomAccess, maxCapacity, players) {
    companion object {
        fun create(command: CreateGameRoomCommand): GameRoom {
            return GameRoom(
                id = randomUUID(),
                name = command.roomName,
                roomAccess = command.roomAccess,
                maxCapacity = command.maxCapacity,
                owner = command.userInfo,
                gameRoomStatus = GameRoomStatus.create(
                    bbAmount = command.bbAmount,
                    sbAmount = command.sbAmount,
                    totalRounds = command.totalRounds,
                )
            )
        }
    }

    fun update(
        command: UpdateGameRoomCommand
    ) {
        this.name = command.roomName
        this.roomAccess = command.roomAccess
        this.maxCapacity = command.maxCapacity
    }

    data class GameRoomStatus(
        private var bbAmount: Int,
        private var sbAmount: Int,
        private var totalRounds: Int = 0, // 총 진행할 라운드 수
    ) {
        companion object {
            fun create(
                bbAmount: Int,
                sbAmount: Int,
                totalRounds: Int
            ) = GameRoomStatus(
                bbAmount = bbAmount,
                sbAmount = sbAmount,
                totalRounds = totalRounds,
            )
        }

        fun getBbAmount(): Int = bbAmount
        fun getSbAmount(): Int = sbAmount
        fun getTotalRounds(): Int = totalRounds

        // 게임방 상태 업데이트
        fun roomUpdate(
            bbAmount: Int,
            sbAmount: Int,
            totalRounds: Int,
        ) = GameRoomStatus(
            bbAmount = bbAmount,
            sbAmount = sbAmount,
            totalRounds = totalRounds,
        )
    }
}




