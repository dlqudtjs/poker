package mong.poker.socketadapter.support.global.config

import TokenManager
import mong.poker.application.global.support.exception.CustomException
import mong.poker.application.global.support.exception.ErrorType
import mong.poker.core.domain.user.UserInfo
import mong.poker.socketadapter.support.global.auth.WebSocketAuthContext
import mong.poker.socketapplication.connection.ConnectionService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.SimpMessageType
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.stereotype.Component
import java.util.*

@Component
class WebSocketMessageInterceptor(
    private val socketAuthContext: WebSocketAuthContext,
    private val tokenManager: TokenManager,
    private val connectionService: ConnectionService,
) : ChannelInterceptor {

    private val logger = LoggerFactory.getLogger(WebSocketMessageInterceptor::class.java)

    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        val accessor = StompHeaderAccessor.wrap(message)

        if (isPassMessageType(accessor)) {
            return message
        }

        if (accessor.sessionId.isNullOrBlank()) {
            logger.warn("세션 ID가 없습니다.")
            throw CustomException(ErrorType.UNAUTHORIZED)
        }

        when (accessor.command) {
            StompCommand.CONNECT -> {
                logger.info("CONNECT 명령 처리")
                handleConnect(accessor)
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
                handleDisconnect(accessor)
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
        socketAuthContext.clear()
    }

    private fun extractUserFromJwt(accessor: StompHeaderAccessor): UserInfo? {
        return runCatching {
            val authHeader = accessor.getNativeHeader(HttpHeaders.AUTHORIZATION)?.firstOrNull()
                ?: return null
            val token = authHeader.removePrefix("Bearer ")

            val claims = tokenManager.verifyToken(token)
            UserInfo(
                id = UUID.fromString(claims["id"].toString()),
                nickname = claims["nickname"].toString(),
            )
        }.getOrNull()
    }

    private fun handleConnect(accessor: StompHeaderAccessor) {
        val userInfo = extractUserFromJwt(accessor)

        if (userInfo == null) {
            logger.warn("${accessor.command}: 유효하지 않은 토큰입니다.")
            throw CustomException(ErrorType.UNAUTHORIZED)
        }

        socketAuthContext.userInfo = userInfo

        connectionService.connect(
            userInfo = userInfo,
            sessionId = accessor.sessionId!!,
        )
    }

    private fun handleDisconnect(accessor: StompHeaderAccessor) {
        connectionService.disconnect(accessor.sessionId!!)
    }

    private fun isPassMessageType(accessor: StompHeaderAccessor): Boolean {
        // 시스템 메시지 타입 무시
        return accessor.messageType in listOf(
            SimpMessageType.CONNECT_ACK,
            SimpMessageType.DISCONNECT_ACK,
            SimpMessageType.HEARTBEAT,
            SimpMessageType.OTHER,
        )
    }
}
