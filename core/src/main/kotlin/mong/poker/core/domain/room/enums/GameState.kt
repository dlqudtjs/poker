package mong.poker.core.domain.room.enums

enum class GameState {
    WAITING,      // 대기중
    PLAYING,      // 게임중
    PAUSED,       // 일시정지
    FINISHED      // 종료
}
