package mong.poker.application.domain.player.service

import mong.poker.application.global.support.exception.CustomException
import mong.poker.application.global.support.exception.ErrorType
import mong.poker.core.domain.player.Player
import mong.poker.core.domain.user.UserInfo
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Service
class PlayerService {
    private val players: MutableMap<UUID, Player> = ConcurrentHashMap()

    companion object {
        private val logger = LoggerFactory.getLogger(PlayerService::class.java)
    }

    fun createPlayer(
        userInfo: UserInfo,
        executedAt: LocalDateTime,
    ): Player {
        val player = Player.create(
            userInfo = userInfo,
            connectedAt = executedAt,
        )

        players[userInfo.id] = player

        logger.info(
            """
            Player 접속
            ├─ Player ID   : ${userInfo.id}
            └─ 닉네임       : ${userInfo.nickname}
            """.trimIndent()
        )

        return player
    }

    fun removePlayerByUserId(userId: UUID) {
        val player = players.remove(userId) ?: throw CustomException(ErrorType.PLAYER_NOT_FOUND)

        logger.info(
            """
            Player 접속 종료
            ├─ Player ID   : $userId
            └─ 닉네임       : ${player.userInfo.nickname} 
            """.trimIndent()
        )
    }

    fun activePlayerCount(): Int {
        return players.size
    }

    fun getPlayerByUserId(userId: UUID): Player? {
        return players[userId]
    }

    fun existsByUserId(userId: UUID): Boolean {
        return players.containsKey(userId)
    }
}
