package mong.poker.webapi.global.auth

import TokenManager
import jakarta.annotation.PostConstruct
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mong.poker.application.global.support.exception.CustomException
import mong.poker.application.global.support.exception.ErrorType
import mong.poker.core.domain.user.UserInfo
import mong.poker.webapi.global.exception.FailureHandler
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import org.springframework.web.cors.CorsUtils
import org.springframework.web.servlet.HandlerInterceptor
import java.util.*

@Component
class AuthInterceptor(
    private val tokenManager: TokenManager,
    private val apiAuthContext: ApiAuthContext,
    private val authNotRequiredConditions: MutableSet<UriAndMethodsCondition> = mutableSetOf(),
) : HandlerInterceptor {

    companion object {
        private val pathMatcher = AntPathMatcher()
    }

    @PostConstruct
    fun configureAuthWhitelist() {
        authNotRequiredConditions += listOf(
            UriAndMethodsCondition("/swagger-ui/**", setOf(HttpMethod.GET)),
            UriAndMethodsCondition("/v3/api-docs/**", setOf(HttpMethod.GET)),
            UriAndMethodsCondition("/api/v1/users/signup", setOf(HttpMethod.POST)),
            UriAndMethodsCondition("/api/v1/users/signin", setOf(HttpMethod.POST)),
        )
    }

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

        val userInfo = extractUserFromJwt(request)
        if (userInfo == null) {
            FailureHandler.handleFailure(CustomException(ErrorType.UNAUTHORIZED), response)
            return false
        }

        apiAuthContext.userInfo = userInfo
        return true
    }

    private fun isAuthenticationNotRequired(request: HttpServletRequest): Boolean {
        val httpMethod = HttpMethod.valueOf(request.method)
        val requestURI = request.requestURI
        return authNotRequiredConditions.any { it.match(requestURI, httpMethod) }
    }

    private fun extractUserFromJwt(request: HttpServletRequest): UserInfo? {
        val authHeader = request.getHeader("Authorization") ?: return null
        if (!authHeader.startsWith("Bearer ")) return null

        return runCatching {
            val token = authHeader.removePrefix("Bearer ")
            val claims = tokenManager.verifyToken(token)
            UserInfo(
                id = UUID.fromString(claims["id"].toString()),
                nickname = claims["nickname"].toString(),
            )
        }.getOrNull()
    }

    data class UriAndMethodsCondition(
        val uriPattern: String,
        val httpMethods: Set<HttpMethod>,
    ) {
        fun match(requestURI: String, httpMethod: HttpMethod): Boolean {
            return pathMatcher.match(uriPattern, requestURI) && httpMethods.contains(httpMethod)
        }
    }
}
