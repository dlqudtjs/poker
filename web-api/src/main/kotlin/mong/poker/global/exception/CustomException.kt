package mong.poker.global.exception

data class CustomException(
    val errorType: ErrorType,
) : RuntimeException()
