package mong.poker.application.domain.player.error

import mong.poker.core.exception.ErrorType

enum class PlayerErrorType(
    override val status: Int,
    override val message: String
) : ErrorType {
    PLAYER_NOT_FOUND(400, "플레이어를 찾을 수 없습니다."),
}
