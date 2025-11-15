package mong.poker.application.domain.room.lobby.service

import mong.poker.application.domain.room.RoomManager
import mong.poker.core.domain.user.UserInfo
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class LobbyService(
    private val roomManager: RoomManager,
) {

    companion object {
        private val logger = LoggerFactory.getLogger(LobbyService::class.java)
    }

    fun enterLobby(userInfo: UserInfo) {

    }

}
