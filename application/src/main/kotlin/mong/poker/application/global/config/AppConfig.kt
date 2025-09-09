package mong.poker.application.global.config

import JwtTokenManager
import TokenManager
import mong.poker.lib.encrypt.PasswordEncoder
import mong.poker.lib.encrypt.impl.BCryptPasswordEncoder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun tokenManager(
        @Value("\${jwt.secret.access}") secret: String,
        @Value("\${jwt.access-token-validity}") accessTokenValidity: Long,
    ): TokenManager = JwtTokenManager(secret, accessTokenValidity)
}
