package mong.poker.core.domain.room

import mong.poker.core.domain.player.Player
import java.util.*

sealed class Room(
    open val id: UUID,
    open val name: String,
    open val roomAccess: GameRoomAccess,
    open val maxCapacity: Int,
    open val players: MutableSet<Player>,
) {
    sealed class GameRoomAccess {
        fun isPrivate(): Boolean {
            return this is Private
        }

        fun isCorrectPassword(inputPassword: String): Boolean {
            return when (this) {
                is Public -> true
                is Private -> this.isCorrectPassword(inputPassword)
            }
        }

        object Public : GameRoomAccess()
        data class Private(val password: String) : GameRoomAccess()
    }
}
