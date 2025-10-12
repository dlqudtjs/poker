package mong.poker.core.domain.room.command

import mong.poker.core.domain.room.GameRoom
import mong.poker.core.domain.user.UserInfo

data class CreateGameRoomCommand(
    val roomName: String,
    val roomAccess: GameRoom.GameRoomAccess,
    val maxPlayerCount: Int,
    val bbAmount: Int,
    val sbAmount: Int,
    val userInfo: UserInfo,
)
