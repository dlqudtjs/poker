package mong.poker.core.domain.user

import java.time.LocalDateTime
import java.util.*

class ConnectedUser(
    val userId: UUID, // 사용자 ID
    val sessionId: String,  // WebSocket 세션 ID
    private var connectionStatus: ConnectionStatus,
    private val connectedAt: LocalDateTime = LocalDateTime.now(),
) {

    fun disconnect() {
        connectionStatus = ConnectionStatus.DISCONNECTED
    }

    fun getConnectionStatus(): ConnectionStatus {
        return connectionStatus
    }
}
