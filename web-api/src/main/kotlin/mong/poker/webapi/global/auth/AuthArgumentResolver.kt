package mong.poker.webapi.global.auth

import mong.poker.core.domain.user.UserInfo
import mong.poker.core.exception.CommonErrorCode
import mong.poker.core.exception.CustomException
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class AuthArgumentResolver(
    private val authContext: ApiAuthContext,
) : HandlerMethodArgumentResolver {

    /**
     * 파라미터에 @ApiRequiredAuth 어노테이션이 붙어 있고 타입이 UserInfo 인 경우에만 해당 리졸버가 작동
     */
    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.hasParameterAnnotation(ApiRequiredAuth::class.java) && parameter.parameterType == UserInfo::class.java

    /**
     * supportsParameter 가 true 일 시 작동하며, 값이 없으면 인증되지 않은 상태이므로 커스텀 예외를 발생
     */
    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): UserInfo? = authContext.userInfo ?: throw CustomException(CommonErrorCode.UNAUTHORIZED)
}
