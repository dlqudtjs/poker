package mong.poker.socketadapter.support.domain.lobby

import mong.poker.application.domain.room.gameroom.service.GameRoomService
import mong.poker.core.domain.user.UserInfo
import mong.poker.socketadapter.support.global.auth.WebSocketRequiredAuth
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller

@Controller
class LobbyWebSocketController(
    private val gameRoomService: GameRoomService,
    private val messagingTemplate: SimpMessagingTemplate,
) {
    // 로비 접속
    @MessageMapping("/lobby/connect")
    fun connectToLobby(
        @WebSocketRequiredAuth userInfo: UserInfo,
        message: String
    ) {
        println(message)
//        val sessionId = session.sessionId!!
//
//        val response = connectToLobbyUseCase.execute(
//            ConnectToLobbyUseCase.Request(playerId, sessionId),
//            LocalDateTime.now()
//        )

        // 본인에게만 현재 방 목록 전송
//        val rooms = gameRoomService.getAllRooms()
//        messagingTemplate.convertAndSendToUser(
//            sessionId,
//            "/queue/lobby/init",
//            LobbyInitResponse(
//                rooms = rooms.map { it.toDto() },
//                connectedUserCount = response.connectedUserCount
//            )
//        )
    }

//    // 로비 채팅
//    @MessageMapping("/lobby/chat")
//    fun sendChatMessage(
//        @Header("Authorization") token: String,
//        @Payload request: ChatMessageRequest,
//    ) {
//        val playerId = authService.validateToken(token)
//
//        sendLobbyChatUseCase.execute(
//            SendLobbyChatUseCase.Request(playerId, request.content),
//            LocalDateTime.now()
//        )
//
//        // 브로드캐스트는 UseCase에서 BroadcastPort를 통해 처리
//    }
}
