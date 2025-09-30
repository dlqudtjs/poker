package mong.poker.application.global.support.exception


enum class ErrorType(
    val status: Int,
    val message: String,
) {
    // user 관련
    DUPLICATED_NICKNAME(400, "Duplicated nickname"),
    DUPLICATED_ACCOUNT_ID(400, "Duplicated account id"),
    INVALID_LOGIN_INFO(400, "Invalid login info"),
    USER_NOT_FOUND(404, "User not found"),

    // room 관련
    ROOM_NOT_FOUND(400, "Room not found"),

    UNCAUGHT_EXCEPTION(500, "Uncaught exception"),
    BAD_REQUEST(400, "Bad request"),
    NOT_FOUND(404, "Not found"),
    UNAUTHORIZED(401, "Unauthorized"),
}
