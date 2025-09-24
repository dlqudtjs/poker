package mong.poker.core.domain.room.command

import mong.poker.core.domain.room.GameRoom
import java.util.*

data class CreateGameRoomCommand(
    val roomName: String,
    val roomAccess: GameRoom.GameRoomAccess,
    val maxPlayerCount: Int,
    val bbAmount: Int,
    val sbAmount: Int,
    val hostUserId: UUID,
)
