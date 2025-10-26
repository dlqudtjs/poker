package mong.poker.application.domain.room

import mong.poker.application.domain.room.gameroom.service.GameRoomService
import mong.poker.application.domain.room.lobby.service.LobbyService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RoomManager(
    private val lobbyService: LobbyService,
    private val gameRoomService: GameRoomService,
) {

    companion object {
        private val logger = LoggerFactory.getLogger(RoomManager::class.java)
    }

}
