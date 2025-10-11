package mong.poker.socketadapter.support.global.auth

import mong.poker.application.global.support.exception.CustomException
import mong.poker.application.global.support.exception.ErrorType
import org.springframework.core.MethodParameter
import org.springframework.messaging.Message
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver
import org.springframework.stereotype.Component
import java.util.*

@Component
class WebSocketAuthArgumentResolver(
    private val webSocketAuthContext: WebSocketAuthContext,
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(WebSocketRequiredAuth::class.java)
                && parameter.parameterType == UUID::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        message: Message<*>
    ): UUID {
        return webSocketAuthContext.id
            ?: throw CustomException(ErrorType.UNAUTHORIZED)
    }
}
