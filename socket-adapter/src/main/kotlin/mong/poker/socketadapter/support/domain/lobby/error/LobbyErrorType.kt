package mong.poker.socketadapter.support.domain.lobby.error

import mong.poker.core.exception.ErrorType

enum class LobbyErrorType(
    override val status: Int,
    override val message: String
) : ErrorType {
    INVALID_MESSAGE_FORMAT(400, "메시지 형식이 올바르지 않습니다."),
}
