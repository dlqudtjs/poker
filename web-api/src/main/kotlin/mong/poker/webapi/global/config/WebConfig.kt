package mong.poker.webapi.global.config

import mong.poker.webapi.global.auth.AuthArgumentResolver
import mong.poker.webapi.global.auth.AuthInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration(proxyBeanMethods = false)
class WebConfig(
    private val authArgumentResolver: AuthArgumentResolver,
    private val authInterceptor: AuthInterceptor,
) : WebMvcConfigurer {
    @Value("\${swagger-server}")
    private val swaggerServer: String? = null

    // 컨트롤러 메서드의 파라미터를 해석하는 커스텀 리졸버 추가
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(authArgumentResolver)
    }

    // 모든 HTTP 요청 전에 실행되는 인터셉터
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins(
                "http://localhost:3000",
                swaggerServer
            )
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
    }
}
