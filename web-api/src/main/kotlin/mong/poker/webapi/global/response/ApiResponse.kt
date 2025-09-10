package mong.poker.webapi.global.response

import mong.poker.application.global.support.exception.CustomException
import mong.poker.application.global.support.exception.ErrorType

/**
 * API 의 일관된 응답을 주기 위한 클래스
 */
sealed class ApiResponse<T> {
    data class Success<T>(val data: T?) : ApiResponse<T>()
    data class SuccessList<T>(val pageSize: Int, val page: Int, val data: List<T>) : ApiResponse<T>()
    data class Error<T>(val status: Int, val message: String) : ApiResponse<Nothing>()

    companion object {
        fun <T> success(data: T?) = Success(data)

        fun <T> successList(
            pageSize: Int,
            page: Int,
            data: List<T>,
        ) = SuccessList(pageSize, page, data)

        fun <T> error(
            errorType: ErrorType,
        ): Error<T> {
            return Error(errorType.status, errorType.message)
        }

        fun <T> error(exception: CustomException): Error<T> {
            return Error(exception.errorType.status, exception.errorType.message)
        }

        fun <T> error(status: Int, message: String): Error<T> {
            return Error(status, message)
        }
    }
}
