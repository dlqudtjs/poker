package mong.poker.socketadapter.support.domain.lobby.subscribe

import mong.poker.core.domain.user.UserInfo
import mong.poker.socketadapter.support.domain.lobby.LobbyEndpoint
import mong.poker.socketadapter.support.global.config.subscribe.SubscribeHandler
import mong.poker.socketadapter.support.global.message.MessagePayload
import org.springframework.context.annotation.Lazy
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class LobbySubscribeHandler(
    @Lazy private val messagingTemplate: SimpMessagingTemplate,
) : SubscribeHandler {

    override fun supports(destination: String): Boolean {
        return destination == LobbyEndpoint.LOBBY_SUBSCRIBE
    }

    override fun handleSubscribe(destination: String, userInfo: UserInfo) {
        val message = MessagePayload(
            senderInfo = userInfo,
            message = "${userInfo.nickname} 유저가 로비에 입장했습니다.",
        )

        messagingTemplate.convertAndSend(destination, message)
    }
}
