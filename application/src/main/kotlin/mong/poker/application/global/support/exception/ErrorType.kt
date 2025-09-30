package mong.poker.application.global.support.exception


enum class ErrorType(
    val status: Int,
    val message: String,
) {
    // user 관련
    DUPLICATED_NICKNAME(400, "닉네임이 이미 존재합니다."),
    DUPLICATED_ACCOUNT_ID(400, "계정 ID가 이미 존재합니다."),
    INVALID_LOGIN_INFO(400, "로그인 정보가 올바르지 않습니다."),
    USER_NOT_FOUND(400, "유저를 찾을 수 없습니다."),

    // room 관련
    ROOM_NOT_FOUND(400, "비밀번호가 일치하지 않습니다."),

    UNCAUGHT_EXCEPTION(500, "알 수 없는 오류입니다."),
    BAD_REQUEST(400, "올바르지 않은 요청입니다."),
    NOT_FOUND(404, "요청하신 리소스를 찾을 수 없습니다."),
    UNAUTHORIZED(401, "인증이 필요합니다."),
}
