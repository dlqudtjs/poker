package mong.poker.socketadapter.support.domain.connection

import mong.poker.application.domain.player.service.PlayerService
import mong.poker.core.domain.user.UserInfo
import mong.poker.socketadapter.support.domain.connection.session.SessionManager
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ConnectionService(
    private val playerService: PlayerService,
    private val sessionManager: SessionManager,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(ConnectionService::class.java)
    }

    /**
     * CONNECT 시 호출
     * sessionId와 userId를 매핑하여 저장
     */
    fun connect(userInfo: UserInfo, sessionId: String) {
        logger.info("session 및 player 연결: nickname=${userInfo.nickname}, sessionId=$sessionId")

        // 기존 연결이 있으면 제거
        if (sessionManager.existsSessionByUserId(userInfo.id)) {
            logger.info("$userInfo.nickname 의 기존 세션 존재 제거")
            sessionManager.removeSessionByUserId(userInfo.id)
        }

        // 새로운 플레이어 생성
        playerService.createPlayer(
            userInfo = userInfo,
            executedAt = LocalDateTime.now(),
        )
        // 세션 매핑 등록
        sessionManager.registerSession(
            userInfo = userInfo,
            sessionId = sessionId,
        )

        logger.info("사용자 저장 완료: 총 ${playerService.activePlayerCount()}명 연결 중")
    }

    /**
     * DISCONNECT 시 호출
     * sessionId 으로 사용자 정보 삭제
     */
    fun disconnect(sessionId: String) {
        val sessionInfo = sessionManager.getSessionInfoBySessionId(sessionId) ?: return

        val player = playerService.getPlayerByUserId(sessionInfo.userInfo.id) ?: return
        logger.info("session 및 player 연결 해제: nickname=${player.userInfo.nickname}")

        playerService.removePlayerByUserId(player.userInfo.id)
        sessionManager.removeSessionByUserId(player.userInfo.id)

        logger.info("session 및 player 연결 해제 완료")
    }
}
