package mong.poker.core.domain.user

import java.time.LocalDateTime
import java.util.*

class ConnectedUser(
    val userId: UUID, // 사용자 ID
    val sessionId: String,  // WebSocket 세션 ID
    private var connectionStatus: ConnectionStatus,
    private var currentLocation: UserLocation,
    private val connectedAt: LocalDateTime = LocalDateTime.now(),
) {
    sealed class UserLocation {
        // 아무방에 속하지 않음 (로그인 직후)
        object Idle : UserLocation() {
            override fun toString(): String {
                return "대기"
            }
        }

        object Lobby : UserLocation() {
            override fun toString(): String {
                return "로비"
            }
        }

        data class InRoom(val roomId: UUID) : UserLocation() {
            override fun toString(): String {
                return "룸: ($roomId)"
            }
        }

        data class InGame(val gameId: UUID) : UserLocation() {
            override fun toString(): String {
                return "게임: ($gameId)"
            }
        }
    }

    fun disconnect() {
        connectionStatus = ConnectionStatus.DISCONNECTED
    }

    fun getConnectionStatus(): ConnectionStatus {
        return connectionStatus
    }

    fun getCurrentLocation(): UserLocation {
        return currentLocation
    }
}
