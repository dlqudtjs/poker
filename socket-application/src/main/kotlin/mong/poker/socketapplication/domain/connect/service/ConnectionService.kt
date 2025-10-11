package mong.poker.socketapplication.domain.connect.service

import mong.poker.core.domain.user.ConnectedUser
import mong.poker.core.domain.user.ConnectedUser.UserLocation
import mong.poker.core.domain.user.ConnectionStatus
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Service
class ConnectionService {
    private val logger = LoggerFactory.getLogger(ConnectionService::class.java)

    // userId -> ConnectedUser
    private val connectedUsers: MutableMap<UUID, ConnectedUser> = ConcurrentHashMap()

    // sessionId -> userId
    private val sessionToUserId: MutableMap<String, UUID> = ConcurrentHashMap()

    /**
     * CONNECT 시 호출
     * sessionId와 userId를 매핑하여 저장
     */
    fun connect(userId: UUID, sessionId: String): ConnectedUser {
        logger.info("사용자 연결: userId=$userId, sessionId=$sessionId")

        // 기존 연결이 있으면 제거
        // todo: 재연결 처리 로직 구현
        if (connectedUsers.containsKey(userId)) {
            val oldSession = connectedUsers[userId]!!.sessionId
            logger.warn("⚠️ 기존 연결 존재: oldSessionId=$oldSession")
            disconnect(oldSession)
        }

        // ConnectedUser 생성
        val connectedUser = ConnectedUser(
            userId = userId,
            sessionId = sessionId,
            connectionStatus = ConnectionStatus.CONNECTED,
            currentLocation = UserLocation.Lobby
        )

        // 양방향 저장
        connectedUsers[userId] = connectedUser
        sessionToUserId[sessionId] = userId

        logger.info("사용자 저장 완료: 총 ${connectedUsers.size}명 연결 중")
        return connectedUser
    }

    /**
     * DISCONNECT 시 호출
     * sessionId 으로 사용자 정보 삭제
     */
    fun disconnect(sessionId: String) {
        logger.info("연결 해제: sessionId=$sessionId")

        val userId = sessionToUserId[sessionId]

        if (userId == null) {
            logger.warn("sessionId에 해당하는 userId를 찾을 수 없음: $sessionId")
            return
        }

        // ConnectedUser 가져오기
        val connectedUser = connectedUsers[userId]
        if (connectedUser == null) {
            logger.warn("userId에 해당하는 ConnectedUser 를 찾을 수 없음: $userId")
            sessionToUserId.remove(sessionId)
            return
        }

        logger.info("사용자 연결 해제: userId=$userId, 위치=${connectedUser.getCurrentLocation()}")

        // 현재 위치에 따라 추가 처리
        when (val location = connectedUser.getCurrentLocation()) {
            is UserLocation.Lobby -> {
                logger.info("로비에서 퇴장")
            }

            is UserLocation.InRoom -> {
                logger.warn("방에서 퇴장: roomId=${location.roomId}")
            }

            is UserLocation.InGame -> {
                logger.error("게임 중 연결 끊김: gameId=${location.gameId}")
            }
        }

        // 양방향 삭제
        connectedUsers.remove(userId)
        sessionToUserId.remove(sessionId)

        logger.info("연결 해제 완료")
    }
}
