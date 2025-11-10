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

    // player 관련
    PLAYER_NOT_FOUND(400, "플레이어를 찾을 수 없습니다."),

    // stomp 관련
    INVALID_SUBSCRIPTION(400, "유효하지 않은 구독입니다."),
    CANNOT_PROCESS_SUBSCRIPTION(400, "구독을 처리할 수 없습니다."),

    // session 관련
    // 세션 동기화 문제로 인해 발생하는 예외
    SESSION_NOT_FOUND(400, "세션을 찾을 수 없습니다."),

    // room 관련
    ROOM_NOT_FOUND(400, "비밀번호가 일치하지 않습니다."),

    // message 관련
    INVALID_MESSAGE_FORMAT(400, "메시지 형식이 올바르지 않습니다."),

    UNCAUGHT_EXCEPTION(500, "알 수 없는 오류입니다."),
    BAD_REQUEST(400, "올바르지 않은 요청입니다."),
    NOT_FOUND(404, "요청하신 리소스를 찾을 수 없습니다."),
    UNAUTHORIZED(401, "인증이 필요합니다."),
}
