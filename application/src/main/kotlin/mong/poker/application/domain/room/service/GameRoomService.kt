package mong.poker.application.domain.room.service

import mong.poker.core.domain.room.GameRoom
import mong.poker.core.domain.room.command.CreateGameRoomCommand
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class GameRoomService {

    companion object {
        private val logger = LoggerFactory.getLogger(GameRoomService::class.java)
        private val gameRooms = mutableMapOf<UUID, GameRoom>()
    }

    @Transactional(propagation = Propagation.REQUIRED)
    fun createRoom(createGameRoomCommand: CreateGameRoomCommand) {
        val room = GameRoom.create(createGameRoomCommand)
        gameRooms[room.id] = room

        logger.info("게임 방 생성 Room ID: ${room.id}, Room Name: ${room.roomName}")
    }
}
