package mong.poker.application.global.support.exception

data class CustomException(
    val errorType: ErrorType,
) : RuntimeException()
