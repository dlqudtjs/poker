package mong.poker.socketapplication.session

import mong.poker.core.domain.user.UserInfo
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Service
class SessionManager {
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
            sessionMetadata.remove(sessionId)
        }
    }

    data class SessionInfo(
        val userInfo: UserInfo,
        val connectedAt: Instant,
        val lastActivityAt: Instant
    )
}
