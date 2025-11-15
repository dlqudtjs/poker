package mong.poker.core.domain.room.command

import mong.poker.core.domain.room.Room
import mong.poker.core.domain.user.UserInfo
import java.util.*

data class UpdateGameRoomCommand(
    val roomId: UUID,
    val roomName: String,
    val roomAccess: Room.GameRoomAccess,
    val maxCapacity: Int,
    val bbAmount: Int,
    val sbAmount: Int,
    val totalRounds: Int,
    val userInfo: UserInfo,
)
