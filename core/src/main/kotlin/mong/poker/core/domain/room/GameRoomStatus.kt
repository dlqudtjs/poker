package mong.poker.core.domain.room

import mong.poker.core.domain.room.enums.GameState

data class GameRoomStatus(
    private var bbAmount: Int,
    private var sbAmount: Int,
    private var maxPlayerCount: Int,
    private var currentPlayerCount: Int = 0, // 현재 접속한 플레이어 수
    private var gameState: GameState = GameState.WAITING,
    private var totalRounds: Int = 0, // 총 진행된 라운드 수
    private var currentRound: GameRound? = null  // 현재 진행 중인 라운드
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

    // 게임방 상태 업데이트
    fun roomUpdate(
        bbAmount: Int,
        sbAmount: Int,
        maxPlayerCount: Int
    ) = GameRoomStatus(
        bbAmount = bbAmount,
        sbAmount = sbAmount,
        maxPlayerCount = maxPlayerCount,
    )

    fun getBbAmount(): Int {
        return bbAmount
    }

    fun getSbAmount(): Int {
        return sbAmount
    }

    fun getMaxPlayerCount(): Int {
        return maxPlayerCount
    }
}
