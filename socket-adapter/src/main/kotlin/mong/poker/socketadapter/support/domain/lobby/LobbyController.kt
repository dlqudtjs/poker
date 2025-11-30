package mong.poker.socketadapter.support.domain.lobby

import com.fasterxml.jackson.databind.ObjectMapper
import mong.poker.core.domain.user.UserInfo
import mong.poker.core.exception.CommonErrorCode
import mong.poker.core.exception.CustomException
import mong.poker.socketadapter.support.domain.lobby.error.LobbyErrorType
import mong.poker.socketadapter.support.global.message.MessagePayload
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.stereotype.Controller

@Controller
class LobbyController {
    companion object {
        const val LOBBY_SUBSCRIBE = "/topic/lobby"
        const val LOBBY_MESSAGE = "/lobby/message"
        private val objectMapper = ObjectMapper()
    }

    @MessageMapping(LOBBY_MESSAGE)
    @SendTo(LOBBY_SUBSCRIBE)
    fun handleLobbyMessage(
        @Payload message: String,
        headerAccessor: SimpMessageHeaderAccessor
    ): MessagePayload {
        val userInfo = headerAccessor.sessionAttributes?.get("userInfo") as? UserInfo
            ?: throw CustomException(CommonErrorCode.UNAUTHORIZED)

        val readTree = objectMapper.readTree(message)
        val messageContent = readTree.get("message")?.asText()
            ?: throw CustomException(LobbyErrorType.INVALID_MESSAGE_FORMAT)

        return MessagePayload(
            senderInfo = userInfo,
            message = messageContent,
        )
    }
}
