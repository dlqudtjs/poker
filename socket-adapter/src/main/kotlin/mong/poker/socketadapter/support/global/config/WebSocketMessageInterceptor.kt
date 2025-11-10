package mong.poker.socketadapter.support.global.config

import TokenManager
import mong.poker.application.global.support.exception.CustomException
import mong.poker.application.global.support.exception.ErrorType
import mong.poker.core.domain.user.UserInfo
import mong.poker.socketadapter.support.domain.connection.ConnectionService
import mong.poker.socketadapter.support.domain.connection.session.SessionManager
import mong.poker.socketadapter.support.global.auth.WebSocketAuthContext
import mong.poker.socketadapter.support.global.config.subscribe.SubscribeFactory
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
    private val subscribeFactory: SubscribeFactory,
    private val sessionManager: SessionManager,
) : ChannelInterceptor {

    private val logger = LoggerFactory.getLogger(WebSocketMessageInterceptor::class.java)

    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        try {

            val accessor = StompHeaderAccessor.wrap(message)

            if (isPassMessageType(accessor)) {
                return message
            }

            val sessionId = accessor.sessionId

            if (sessionId.isNullOrBlank()) {
                logger.warn("세션 ID가 없습니다.")
                throw CustomException(ErrorType.UNAUTHORIZED)
            }

            when (accessor.command) {
                StompCommand.CONNECT -> {
                    handleConnect(accessor)
                }

                StompCommand.SEND -> {
                    setUserInfoToContext(accessor)
                }

                StompCommand.SUBSCRIBE -> {
                    setUserInfoToContext(accessor)
                    handleSubscribe(accessor)
                }

                StompCommand.UNSUBSCRIBE -> {
                    setUserInfoToSessionAttributes(accessor)
                }

                StompCommand.DISCONNECT -> {
                    // 클라이언트에서 보낸 DISCONNECT 명령어만 처리
                    if (!accessor.getNativeHeader("receipt").isNullOrEmpty()) {
                        handleDisconnect(accessor)
                    } else {
                        // 서버에서 클라이언트로 보내는 응답은 무시함
                    }
                }

                else -> {
                    logger.info("알 수 없는 명령어: ${accessor.command}")
                    println(accessor)
                }
            }

            return message
        } catch (ex: Exception) {
            logger.error("WebSocket 메시지 처리 중 오류 발생: ${ex.message}", ex)
            throw ex
        }
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

    private fun handleSubscribe(accessor: StompHeaderAccessor) {
        val userInfo = socketAuthContext.userInfo
            ?: throw CustomException(ErrorType.UNAUTHORIZED)

        val destination = accessor.destination
            ?: throw CustomException(ErrorType.INVALID_SUBSCRIPTION)

        // 구독 핸들러가 없으면 무시
        val handler = subscribeFactory.getHandler(destination)
            ?: throw CustomException(ErrorType.CANNOT_PROCESS_SUBSCRIPTION)

        handler.handleSubscribe(destination, userInfo)
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
            SimpMessageType.MESSAGE,
            SimpMessageType.OTHER,
        )
    }

    private fun setUserInfoToContext(accessor: StompHeaderAccessor) {
        val userInfo = sessionManager.getSessionInfoBySessionId(accessor.sessionId!!)?.userInfo
            ?: throw CustomException(ErrorType.UNAUTHORIZED)
        socketAuthContext.userInfo = userInfo
        accessor.sessionAttributes?.put("userInfo", userInfo)
    }

    private fun setUserInfoToSessionAttributes(accessor: StompHeaderAccessor) {
        val userInfo = sessionManager.getSessionInfoBySessionId(accessor.sessionId!!)?.userInfo
            ?: throw CustomException(ErrorType.UNAUTHORIZED)
        accessor.sessionAttributes?.put("userInfo", userInfo)
    }
}
