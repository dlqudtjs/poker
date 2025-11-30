package mong.poker.socketadapter.support.domain.connection.error

import mong.poker.core.exception.ErrorType

enum class SessionErrorType(
    override val status: Int,
    override val message: String
) : ErrorType {
    SESSION_NOT_FOUND(400, "세션을 찾을 수 없습니다."),
}
