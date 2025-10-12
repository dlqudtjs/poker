package mong.poker.application.domain.room.lobby.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class LobbyService {

    companion object {
        private val logger = LoggerFactory.getLogger(LobbyService::class.java)
        private val lobbyUsers = mutableSetOf<UUID>()
    }

    fun enterLobby(userId: UUID) {
        lobbyUsers.add(userId)
        logger.info("사용자 $userId 님이 로비에 입장했습니다. 현재 로비 인원: ${lobbyUsers.size}")
    }

}
