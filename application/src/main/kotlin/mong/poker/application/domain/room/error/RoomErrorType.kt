package mong.poker.application.domain.room.error

import mong.poker.core.exception.ErrorType

enum class RoomErrorType(
    override val status: Int,
    override val message: String
) : ErrorType {
    ROOM_NOT_FOUND(400, "방을 찾을 수 없습니다."),
    INVALID_ROOM_OPERATION(400, "유효하지 않은 방 작업입니다."),
    PLAYER_NOT_FOUND(400, "플레이어를 찾을 수 없습니다."),
}
