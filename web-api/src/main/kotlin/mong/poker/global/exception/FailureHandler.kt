package mong.poker.global.exception

import jakarta.servlet.http.HttpServletResponse
import mong.poker.application.global.support.exception.CustomException
import mong.poker.application.global.support.exception.ErrorType
import mong.poker.global.util.ExceptionUtil
import org.apache.logging.log4j.LogManager

/**
 * Global Exception Handler 를 통해 처리되지 않은 예외를 처리하는 클래스
 *
 * Spring Security 의 필터 체인이나 인터셉터에서 발생하는 예외를 처리하는 역할
 */
object FailureHandler {
    private val log = LogManager.getLogger(FailureHandler::class.java)

    fun handleFailure(
        throwable: Throwable,
        response: HttpServletResponse,
    ) {
        when (throwable) {
            is CustomException -> {
                ExceptionUtil.writeErrorJson(
                    response,
                    throwable.errorType,
                )
                throw throwable
            }

            else -> {
                log.error(throwable.stackTraceToString())
                ExceptionUtil.writeErrorJson(
                    response,
                    ErrorType.UNCAUGHT_EXCEPTION,
                )
                throw throwable
            }
        }
    }
}
