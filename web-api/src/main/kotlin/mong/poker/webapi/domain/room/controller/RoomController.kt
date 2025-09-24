package mong.poker.webapi.domain.room.controller

import jakarta.validation.Valid
import mong.poker.application.domain.room.usecase.CreateGameRoomUseCase
import mong.poker.webapi.domain.room.controller.request.CreateRoomRequest
import mong.poker.webapi.global.auth.ApiRequiredAuth
import mong.poker.webapi.global.response.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/v1/rooms")
class RoomController(
    private val createGameRoomUseCase: CreateGameRoomUseCase
) {

    @PostMapping
    fun createRoom(
        @ApiRequiredAuth userId: UUID,
        @RequestBody @Valid request: CreateRoomRequest
    ): ResponseEntity<ApiResponse<CreateGameRoomUseCase.Response>> {
        val response = createGameRoomUseCase.execute(
            request = CreateGameRoomUseCase.Request(
                hostUserId = userId,
                roomName = request.title,
                password = request.password,
                maxUserCount = request.maxPlayerCount,
                bbAmount = request.bbAmount,
                sbAmount = request.sbAmount,
            ),
            executedAt = java.time.LocalDateTime.now(),
        )
        
        return ResponseEntity.ok(ApiResponse.success(response))
    }
}
