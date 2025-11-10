package mong.poker.socketadapter.support.domain.lobby

import com.fasterxml.jackson.databind.ObjectMapper
import mong.poker.application.global.support.exception.CustomException
import mong.poker.application.global.support.exception.ErrorType
import mong.poker.core.domain.user.UserInfo
import mong.poker.socketadapter.support.global.message.MessagePayload
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.stereotype.Controller

@Controller
class LobbyController {
    companion object {
        private val objectMapper = ObjectMapper()
    }

    @MessageMapping(LobbyEndpoint.LOBBY_MESSAGE)
    @SendTo(LobbyEndpoint.LOBBY_SUBSCRIBE)
    fun handleLobbyMessage(
        @Payload message: String,
        headerAccessor: SimpMessageHeaderAccessor
    ): MessagePayload {
        val userInfo = headerAccessor.sessionAttributes?.get("userInfo") as? UserInfo
            ?: throw CustomException(ErrorType.UNAUTHORIZED)

        val readTree = objectMapper.readTree(message)
        val messageContent = readTree.get("message")?.asText()
            ?: throw CustomException(ErrorType.INVALID_MESSAGE_FORMAT)

        return MessagePayload(
            senderInfo = userInfo,
            message = messageContent,
        )
    }
}
