package mong.poker.webapi.domain.room.controller

import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import mong.poker.application.domain.room.usecase.CreateGameRoomUseCase
import mong.poker.application.domain.room.usecase.GetGameRoomListUseCase
import mong.poker.webapi.domain.room.controller.request.CreateRoomRequest
import mong.poker.webapi.global.auth.ApiRequiredAuth
import mong.poker.webapi.global.response.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/rooms")
class RoomController(
    private val createGameRoomUseCase: CreateGameRoomUseCase,
    private val getGameRoomListUseCase: GetGameRoomListUseCase,
) {

    @Operation(summary = "게임 방 생성")
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

    @Operation(summary = "게임 방 목록 조회")
    @GetMapping("/list")
    fun getRoomList(): ResponseEntity<ApiResponse<GetGameRoomListUseCase.Response>> {
        val response = getGameRoomListUseCase.execute(
            request = Unit,
            executedAt = java.time.LocalDateTime.now(),
        )

        return ResponseEntity.ok(ApiResponse.success(response))
    }
}
