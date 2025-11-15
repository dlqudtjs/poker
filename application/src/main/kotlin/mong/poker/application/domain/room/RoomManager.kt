package mong.poker.application.domain.room

import mong.poker.application.domain.player.service.PlayerService
import mong.poker.application.global.support.exception.CustomException
import mong.poker.application.global.support.exception.ErrorType
import mong.poker.core.domain.player.Player
import mong.poker.core.domain.room.GameRoom
import mong.poker.core.domain.room.LobbyRoom
import mong.poker.core.domain.room.Room
import mong.poker.core.domain.room.command.CreateGameRoomCommand
import mong.poker.core.domain.room.command.UpdateGameRoomCommand
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class RoomManager(
    private val playerService: PlayerService,
) {
    private val rooms: MutableMap<UUID, Room> = mutableMapOf()
    private val userLocationMap: MutableMap<Player, Room> = mutableMapOf()

    init {
        rooms[UUID.randomUUID()] = LobbyRoom
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    fun createGameRoom(command: CreateGameRoomCommand): UUID {
        val roomId = UUID.randomUUID()
        val gameRoom = GameRoom.create(command)

        rooms[roomId] = gameRoom
        gameRoom.joinUser(command.userInfo.id)

        logger.info(
            """
            게임 방 생성
            ├─ Room ID   : ${gameRoom.id}
            ├─ Room Name : ${gameRoom.name}
            └─ 방장       : ${command.userInfo.nickname}
            """.trimIndent()
        )

        return roomId
    }

    fun updateGameRoom(command: UpdateGameRoomCommand): UUID {
        val room = rooms[command.roomId] ?: throw CustomException(ErrorType.ROOM_NOT_FOUND)

        when (room) {
            is LobbyRoom -> throw CustomException(ErrorType.INVALID_ROOM_OPERATION)
            is GameRoom -> room.update(command)
        }

        return command.roomId
    }

    fun getAllGameRoom(): List<GameRoom> {
        return rooms.values.filterIsInstance<GameRoom>()
    }

    private fun Room.joinUser(userId: UUID) {
        val player = playerService.getPlayerByUserId(userId)
            ?: throw CustomException(ErrorType.PLAYER_NOT_FOUND)

        this.players.add(player)
        userLocationMap[player] = this
    }
}
