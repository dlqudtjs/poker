package mong.poker.socketadapter.support.global.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig(
    private val webSocketMessageInterceptor: WebSocketMessageInterceptor,
) : WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        // /topic으로 시작하는 메시지는 메시지 브로커가 처리
        registry.enableSimpleBroker("/topic")
        registry.setApplicationDestinationPrefixes("/app") // 클라이언트 → 서버 전송 prefix
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        // 클라이언트가 WebSocket 연결을 시도할 때 사용할 엔드포인트 URL 경로
        registry.addEndpoint("/ws")
            .setAllowedOriginPatterns("*")
            .withSockJS() // SockJS fallback 지원
    }

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        // 클라이언트에서 서버로 오는 메시지를 인터셉트
        registration.interceptors(webSocketMessageInterceptor)
    }

    override fun configureClientOutboundChannel(registration: ChannelRegistration) {
        // 서버에서 클라이언트로 가는 메시지를 인터셉트
        registration.interceptors(webSocketMessageInterceptor)
    }
}
