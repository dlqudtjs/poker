package mong.poker.core.exception

enum class CommonErrorCode(
    override val status: Int,
    override val message: String
) : ErrorType {
    UNAUTHORIZED(401, "인증이 필요합니다"),
    UNCAUGHT_EXCEPTION(500, "알 수 없는 오류입니다."),
    BAD_REQUEST(400, "잘못된 요청입니다."),
    NOT_FOUND(404, "요청한 리소스를 찾을 수 없습니다."),
}
