package mong.poker.core.domain.room.error

import mong.poker.core.exception.ErrorType

enum class RoomErrorType(
    override val status: Int,
    override val message: String
) : ErrorType {
    NOT_ROOM_HOST(403, "방장이 아닙니다."),
}
