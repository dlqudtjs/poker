package mong.poker.core.domain.room

import mong.poker.core.domain.room.enums.RoundPhase

data class GameRound(
    val roundNumber: Int, // 라운드 번호
//    val communityCards: List<Card> = emptyList(),
    val pot: Int = 0, // 팟 금액
    val currentBet: Int = 0, // 현재 베팅 금액
    val dealerPosition: Int, // 딜러 위치 (플레이어 인덱스)
    val currentPlayerTurn: Int, // 현재 턴인 플레이어 위치 (플레이어 인덱스)
    val roundPhase: RoundPhase = RoundPhase.PRE_FLOP, // 라운드 단계
//    val players: List<PlayerInRound> = emptyList()
)
