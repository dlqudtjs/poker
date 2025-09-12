package mong.poker.socketadapter.support.global.config

import TokenManager
import mong.poker.socketadapter.support.global.auth.AuthContext
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor

@Component
class WebSocketAuthInterceptor(
    private val authContext: AuthContext,
    private val tokenManager: TokenManager,
) : HandshakeInterceptor {

    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>,
    ): Boolean {
        val userId = extractUserFromJwt(request)

        if (userId == null) {
            return false
        }

        authContext.id = userId
        return true
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: Exception?,
    ) {
        authContext.clear()
    }

    private fun extractUserFromJwt(request: ServerHttpRequest): String? {
        return runCatching {
            val token = request.headers.getFirst("Authorization")?.removePrefix("Bearer ")
                ?: return null

            val claims = tokenManager.verifyToken(token)
            claims["id"]?.toString()
        }.getOrNull()
    }
}
