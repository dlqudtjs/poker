package mong.poker.global.util

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletResponse
import mong.poker.application.global.support.exception.ErrorType
import mong.poker.global.response.ApiResponse
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import java.nio.charset.StandardCharsets

class ExceptionUtil {
    companion object {
        fun writeErrorJson(
            response: HttpServletResponse,
            errorType: ErrorType,
        ) {
            val errorResponse = ApiResponse.error<Nothing>(
                errorType = errorType,
            )

            val json = ObjectMapper().writeValueAsString(errorResponse)

            response.contentType = APPLICATION_JSON_VALUE
            response.characterEncoding = StandardCharsets.UTF_8.name()
            response.status = errorType.status

            response.writer.write(json)
            response.writer.flush()
            response.writer.close()
        }
    }
}
