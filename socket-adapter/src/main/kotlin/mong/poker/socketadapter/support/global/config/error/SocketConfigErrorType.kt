package mong.poker.socketadapter.support.global.config.error

import mong.poker.core.exception.ErrorType

enum class SocketConfigErrorType(
    override val status: Int,
    override val message: String
) : ErrorType {
    INVALID_SUBSCRIPTION(400, "유효하지 않은 구독입니다."),
    CANNOT_PROCESS_SUBSCRIPTION(400, "구독을 처리할 수 없습니다."),
}
