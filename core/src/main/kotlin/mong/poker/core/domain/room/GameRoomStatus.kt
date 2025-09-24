package mong.poker.core.domain.room

import mong.poker.core.domain.room.enums.GameState

data class GameRoomStatus(
    val bbAmount: Int,
    val sbAmount: Int,
    val maxPlayerCount: Int,
    val currentPlayerCount: Int = 0, // 현재 접속한 플레이어 수
    val gameState: GameState = GameState.WAITING,
    val totalRounds: Int = 0, // 총 진행된 라운드 수
    val currentRound: GameRound? = null  // 현재 진행 중인 라운드
) {
    companion object {
        fun create(
            bbAmount: Int,
            sbAmount: Int,
            maxPlayerCount: Int
        ) = GameRoomStatus(
            bbAmount = bbAmount,
            sbAmount = sbAmount,
            maxPlayerCount = maxPlayerCount
        )
    }
}
