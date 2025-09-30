package mong.poker.application.domain.room.service

import mong.poker.application.global.support.exception.CustomException
import mong.poker.application.global.support.exception.ErrorType
import mong.poker.core.domain.room.GameRoom
import mong.poker.core.domain.room.command.CreateGameRoomCommand
import mong.poker.core.domain.room.command.UpdateGameRoomCommand
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class GameRoomService {

    companion object {
        private val logger = LoggerFactory.getLogger(GameRoomService::class.java)
        private val gameRooms = mutableMapOf<UUID, GameRoom>()
    }

    fun createRoom(createGameRoomCommand: CreateGameRoomCommand): UUID {
        val room = GameRoom.create(createGameRoomCommand)
        gameRooms[room.id] = room

        logger.info("게임 방 생성 Room ID: ${room.id}, Room Name: ${room.getRoomName()}")

        return room.id
    }

    fun getAllRooms(): List<GameRoom> {
        return gameRooms.values.toList()
    }

    fun updateRoom(updateGameRoomCommand: UpdateGameRoomCommand): UUID {
        val room = gameRooms[updateGameRoomCommand.roomId]
            ?: throw CustomException(ErrorType.ROOM_NOT_FOUND)

        room.update(updateGameRoomCommand)

        logger.info("게임 방 수정 Room ID: ${room.id}, Room Name: ${room.getRoomName()}")

        return room.id
    }
}
