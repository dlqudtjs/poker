package mong.poker.socketadapter.support.global.config

import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.SimpMessageType
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.stereotype.Component

@Component
class WebSocketMessageInterceptor : ChannelInterceptor {

    private val logger = LoggerFactory.getLogger(WebSocketMessageInterceptor::class.java)

    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        val accessor = StompHeaderAccessor.wrap(message)

        // 시스템 메시지 타입 무시
        if (accessor.messageType in listOf(
                SimpMessageType.CONNECT_ACK,
                SimpMessageType.HEARTBEAT,
                SimpMessageType.OTHER
            )
        ) {
            return message
        }


        val authHeader = accessor.getNativeHeader(HttpHeaders.AUTHORIZATION)?.firstOrNull()
        val token = authHeader?.removePrefix("Bearer ")
        println(token)

        when (accessor.command) {
            StompCommand.CONNECT -> {
                logger.info("CONNECT 명령어 처리")
            }

            StompCommand.SEND -> {
                logger.info("SEND 명령어 처리")
            }

            StompCommand.SUBSCRIBE -> {
                logger.info("SUBSCRIBE 명령어 처리")
            }

            StompCommand.UNSUBSCRIBE -> {
                logger.info("UNSUBSCRIBE 명령어 처리")
            }

            StompCommand.DISCONNECT -> {
                logger.info("DISCONNECT 명령어 처리")
            }

            else -> {
                logger.info("알 수 없는 명령어: ${accessor.command}")
            }
        }

        return message
    }

    override fun postSend(message: Message<*>, channel: MessageChannel, sent: Boolean) {
    }

    override fun afterSendCompletion(
        message: Message<*>,
        channel: MessageChannel,
        sent: Boolean,
        ex: Exception?
    ) {
    }
}
