package mong.poker.core.exception

data class CustomException(
    val errorType: ErrorType,
) : RuntimeException()
