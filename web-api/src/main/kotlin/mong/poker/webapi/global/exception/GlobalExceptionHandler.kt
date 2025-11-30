package mong.poker.webapi.global.exception

import mong.poker.core.exception.CommonErrorCode
import mong.poker.core.exception.CustomException
import mong.poker.webapi.global.response.ApiResponse
import org.apache.logging.log4j.LogManager
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException

/**
 * REST 컨트롤러에서 발생하는 예외를 전역적으로 처리하는 핸들러 클래스
 *
 * `@RestControllerAdvice`를 통해 스프링 컨텍스트 내 모든 컨트롤러에서 발생하는
 * 예외를 이 핸들러로 모아, 일관된 에러 응답을 반환
 */
@RestControllerAdvice
class GlobalExceptionHandler {
    companion object {
        private val log = LogManager.getLogger(GlobalExceptionHandler::class.java)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResponse.Error<Unit>> {
        printErrorMessage(e)

        val response = ApiResponse.error<Unit>(
            errorType = CommonErrorCode.UNCAUGHT_EXCEPTION,
        )

        return ResponseEntity
            .status(response.status)
            .body(response)
    }

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(e: CustomException): ResponseEntity<ApiResponse.Error<Unit>> {
        printErrorMessage(e)

        val response = ApiResponse.error<Unit>(
            exception = e,
        )

        return ResponseEntity
            .status(e.errorType.status)
            .body(response)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(e: MethodArgumentNotValidException): ResponseEntity<ApiResponse.Error<Unit>> {
        printErrorMessage(e)

        // 첫 번째 필드 에러 메시지를 가져오기
        val firstErrorMessage = e.bindingResult
            .fieldErrors
            .firstOrNull()
            ?.defaultMessage ?: "잘못된 요청입니다."

        val response = ApiResponse.error<Unit>(
            status = HttpStatus.BAD_REQUEST.value(),
            message = firstErrorMessage
        )

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response)
    }

    // JSON 파싱 오류
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(
        ex: HttpMessageNotReadableException
    ): ResponseEntity<ApiResponse.Error<Unit>> {
        printErrorMessage(ex)

        val response = ApiResponse.error<Unit>(
            errorType = CommonErrorCode.BAD_REQUEST,
        )

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response)
    }

    // 404 Not Found 에러 처리
    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFoundException(
        ex: NoHandlerFoundException
    ): ResponseEntity<ApiResponse.Error<Unit>> {
        printErrorMessage(ex)
        val response = ApiResponse.error<Unit>(
            errorType = CommonErrorCode.NOT_FOUND,
        )

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(response)
    }

    /**
     * Exception 객체로부터 에러 메시지를 추출하는 메소드
     */
    private fun printErrorMessage(e: Exception) {
        when (e) {
            is CustomException -> {} // do nothing
            else -> log.error(e.stackTraceToString())
        }
    }
}
