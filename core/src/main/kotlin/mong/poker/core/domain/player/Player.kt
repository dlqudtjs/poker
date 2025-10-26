package mong.poker.core.domain.player

import mong.poker.core.domain.user.UserInfo
import java.time.LocalDateTime
import java.util.*

/**
 * User 에서 연장되는 개념
 * - User: 회원 정보 (닉네임, 잔액 등)
 * - Player: 접속한 사용자 (현재 위치, 접속 시간 등)
 */
class Player(
    val userInfo: UserInfo, // 사용자 ID
    private var currentLocation: UserLocation,
    private val connectedAt: LocalDateTime = LocalDateTime.now(),
) {
    companion object {
        fun create(
            userInfo: UserInfo,
            connectedAt: LocalDateTime,
        ): Player {
            return Player(
                userInfo = userInfo,
                currentLocation = UserLocation.Lobby,
                connectedAt = connectedAt,
            )
        }
    }

    sealed class UserLocation {
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

    fun getCurrentLocation(): UserLocation {
        return currentLocation
    }
}

