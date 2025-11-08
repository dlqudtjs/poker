package mong.poker.socketadapter.support.domain.session

import mong.poker.application.global.support.exception.CustomException
import mong.poker.application.global.support.exception.ErrorType
import mong.poker.core.domain.user.UserInfo
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Service
class SessionManager {
    companion object {
        private val logger = LoggerFactory.getLogger(SessionManager::class.java)
    }

    private val userToSession = ConcurrentHashMap<UUID, String>()
    private val sessionMetadata = ConcurrentHashMap<String, SessionInfo>()

    fun registerSession(userInfo: UserInfo, sessionId: String) {
        userToSession[userInfo.id] = sessionId

        val now = Instant.now()
        sessionMetadata[sessionId] = SessionInfo(
            userInfo = userInfo,
            connectedAt = now,
            lastActivityAt = now
        )

        logger.info(
            """
            세션 등록
            ├─ Session ID  : $sessionId
            └─ 닉네임       : ${userInfo.nickname}
            """.trimIndent()
        )
    }

    fun existsSessionByUserId(userId: UUID): Boolean {
        return userToSession.containsKey(userId)
    }

    fun getSessionInfoBySessionId(sessionId: String): SessionInfo? {
        return sessionMetadata[sessionId]
    }

    fun removeSessionByUserId(userId: UUID) {
        val sessionId = userToSession[userId]

        if (sessionId != null) {
            userToSession.remove(userId)
            val sessionMetaData = sessionMetadata.remove(sessionId)
                ?: throw CustomException(ErrorType.SESSION_NOT_FOUND)

            logger.info(
                """
                세션 제거
                ├─ Session ID  : $sessionId
                └─ 닉네임       : ${sessionMetaData.userInfo.nickname}
                """.trimIndent()
            )
        }
    }

    data class SessionInfo(
        val userInfo: UserInfo,
        val connectedAt: Instant,
        val lastActivityAt: Instant
    )
}
