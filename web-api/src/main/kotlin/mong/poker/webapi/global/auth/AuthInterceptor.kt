package mong.poker.webapi.global.auth

import TokenManager
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsUtils
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthInterceptor(
    private val tokenManager: TokenManager,
    private val socketAuthContext: ApiAuthContext,
    private val authNotRequiredConditions: MutableSet<UriAndMethodsCondition> = mutableSetOf(),
) : HandlerInterceptor {

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        if (CorsUtils.isPreFlightRequest(request)) {
            return true
        }

        if (isAuthenticationNotRequired(request)) {
            return true
        }

        val userId = extractUserFromJwt(request)
        socketAuthContext.id = userId

        return true
    }

    private fun isAuthenticationNotRequired(request: HttpServletRequest): Boolean {
        val httpMethod = HttpMethod.valueOf(request.method)
        val requestURI = request.requestURI
        return authNotRequiredConditions.any { it.match(requestURI, httpMethod) }
    }

    private fun extractUserFromJwt(request: HttpServletRequest): String? {
        val authHeader = request.getHeader("Authorization") ?: return null
        if (!authHeader.startsWith("Bearer ")) return null

        return runCatching {
            val token = authHeader.removePrefix("Bearer ")
            val claims = tokenManager.verifyToken(token)
            claims["id"]?.toString()
        }.getOrNull()
    }

    data class UriAndMethodsCondition(
        val uriPattern: String,
        val httpMethods: Set<HttpMethod>,
    ) {
        fun match(
            requestURI: String,
            httpMethod: HttpMethod,
        ): Boolean {
            return requestURI == uriPattern && httpMethods.contains(httpMethod)
        }
    }
}
