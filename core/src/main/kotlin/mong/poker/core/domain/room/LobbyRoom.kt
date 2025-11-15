package mong.poker.core.domain.room

import java.util.*

object LobbyRoom : Room(
    id = UUID.randomUUID(),
    name = "LOBBY",
    roomAccess = GameRoomAccess.Public,
    players = mutableSetOf(),
    maxCapacity = 100,
)


