package mong.poker.application.global.support.exception


enum class ErrorType(
    val status: Int,
    val message: String,
) {
    // user 관련
    DUPLICATED_NICKNAME(400, "Duplicated nickname"),


    UNCAUGHT_EXCEPTION(500, "Uncaught exception"),
    BAD_REQUEST(400, "Bad request"),
    NOT_FOUND(404, "Not found"),
}
