package mong.poker.application.domain.room.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import mong.poker.application.domain.room.gameroom.service.GameRoomService
import mong.poker.application.domain.room.gameroom.usecase.UpdateGameRoomUseCase
import mong.poker.core.domain.room.GameRoom
import mong.poker.core.domain.room.command.UpdateGameRoomCommand
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

@DisplayName("UpdateGameRoomUseCase 테스트")
class UpdateGameRoomUseCaseTest {

    private lateinit var gameRoomService: GameRoomService
    private lateinit var updateGameRoomUseCase: UpdateGameRoomUseCase
    private lateinit var executedAt: LocalDateTime

    @BeforeEach
    fun setUp() {
        gameRoomService = mockk()
        updateGameRoomUseCase = UpdateGameRoomUseCase(gameRoomService)
        executedAt = LocalDateTime.of(2025, 1, 1, 1, 0, 0)
    }

    @Test
    @DisplayName("공개 방으로 수정 - 비밀번호가 null인 경우")
    fun `should update to public room when password is null`() {
        // given
        val roomId = UUID.randomUUID()
        val request = UpdateGameRoomUseCase.Request(
            roomId = roomId,
            roomName = "수정된 공개 방",
            password = null,
            maxUserCount = 8,
            bbAmount = 2000,
            sbAmount = 1000
        )

        val commandSlot = slot<UpdateGameRoomCommand>()
        every { gameRoomService.updateRoom(capture(commandSlot)) } returns roomId

        // when
        val response = updateGameRoomUseCase.execute(request, executedAt)

        // then
        verify(exactly = 1) { gameRoomService.updateRoom(any()) }

        val capturedCommand = commandSlot.captured
        assertEquals(roomId, capturedCommand.roomId)
        assertEquals("수정된 공개 방", capturedCommand.roomName)
        assertEquals(GameRoom.GameRoomAccess.Public, capturedCommand.roomAccess)
        assertEquals(8, capturedCommand.maxPlayerCount)
        assertEquals(2000, capturedCommand.bbAmount)
        assertEquals(1000, capturedCommand.sbAmount)

        assertEquals(roomId, response.roomId)
    }

    @Test
    @DisplayName("공개 방으로 수정 - 비밀번호가 빈 문자열인 경우")
    fun `should update to public room when password is empty`() {
        // given
        val roomId = UUID.randomUUID()
        val request = UpdateGameRoomUseCase.Request(
            roomId = roomId,
            roomName = "빈 비밀번호 방",
            password = "",
            maxUserCount = 6,
            bbAmount = 1000,
            sbAmount = 500
        )

        val commandSlot = slot<UpdateGameRoomCommand>()
        every { gameRoomService.updateRoom(capture(commandSlot)) } returns roomId

        // when
        val response = updateGameRoomUseCase.execute(request, executedAt)

        // then
        verify(exactly = 1) { gameRoomService.updateRoom(any()) }

        val capturedCommand = commandSlot.captured
        assertEquals(GameRoom.GameRoomAccess.Public, capturedCommand.roomAccess)
        assertEquals(roomId, response.roomId)
    }

    @Test
    @DisplayName("비공개 방으로 수정 - 비밀번호가 있는 경우")
    fun `should update to private room when password is provided`() {
        // given
        val roomId = UUID.randomUUID()
        val password = "newSecret123"
        val request = UpdateGameRoomUseCase.Request(
            roomId = roomId,
            roomName = "수정된 비밀 방",
            password = password,
            maxUserCount = 4,
            bbAmount = 5000,
            sbAmount = 2500
        )

        val commandSlot = slot<UpdateGameRoomCommand>()
        every { gameRoomService.updateRoom(capture(commandSlot)) } returns roomId

        // when
        val response = updateGameRoomUseCase.execute(request, executedAt)

        // then
        verify(exactly = 1) { gameRoomService.updateRoom(any()) }

        val capturedCommand = commandSlot.captured
        assertEquals(roomId, capturedCommand.roomId)
        assertEquals("수정된 비밀 방", capturedCommand.roomName)
        assertEquals(GameRoom.GameRoomAccess.Private(password), capturedCommand.roomAccess)
        assertEquals(4, capturedCommand.maxPlayerCount)
        assertEquals(5000, capturedCommand.bbAmount)
        assertEquals(2500, capturedCommand.sbAmount)

        assertEquals(roomId, response.roomId)
    }

    @Test
    @DisplayName("방 이름만 수정하는 경우")
    fun `should update only room name`() {
        // given
        val roomId = UUID.randomUUID()
        val request = UpdateGameRoomUseCase.Request(
            roomId = roomId,
            roomName = "새로운 방 이름",
            password = "password",
            maxUserCount = 6,
            bbAmount = 1000,
            sbAmount = 500
        )

        val commandSlot = slot<UpdateGameRoomCommand>()
        every { gameRoomService.updateRoom(capture(commandSlot)) } returns roomId

        // when
        val response = updateGameRoomUseCase.execute(request, executedAt)

        // then
        val capturedCommand = commandSlot.captured
        assertEquals("새로운 방 이름", capturedCommand.roomName)
        assertEquals(roomId, response.roomId)
    }

    @Test
    @DisplayName("베팅 금액을 수정하는 경우")
    fun `should update betting amounts`() {
        // given
        val roomId = UUID.randomUUID()
        val request = UpdateGameRoomUseCase.Request(
            roomId = roomId,
            roomName = "베팅 수정 방",
            password = null,
            maxUserCount = 6,
            bbAmount = 10000,
            sbAmount = 5000
        )

        val commandSlot = slot<UpdateGameRoomCommand>()
        every { gameRoomService.updateRoom(capture(commandSlot)) } returns roomId

        // when
        val response = updateGameRoomUseCase.execute(request, executedAt)

        // then
        val capturedCommand = commandSlot.captured
        assertEquals(10000, capturedCommand.bbAmount)
        assertEquals(5000, capturedCommand.sbAmount)
        assertEquals(roomId, response.roomId)
    }

    @Test
    @DisplayName("최대 인원수를 수정하는 경우")
    fun `should update max player count`() {
        // given
        val roomId = UUID.randomUUID()
        val request = UpdateGameRoomUseCase.Request(
            roomId = roomId,
            roomName = "인원 수정 방",
            password = null,
            maxUserCount = 10,
            bbAmount = 1000,
            sbAmount = 500
        )

        val commandSlot = slot<UpdateGameRoomCommand>()
        every { gameRoomService.updateRoom(capture(commandSlot)) } returns roomId

        // when
        val response = updateGameRoomUseCase.execute(request, executedAt)

        // then
        val capturedCommand = commandSlot.captured
        assertEquals(10, capturedCommand.maxPlayerCount)
        assertEquals(roomId, response.roomId)
    }

    @Test
    @DisplayName("공개 방에서 비공개 방으로 변경")
    fun `should change from public to private room`() {
        // given
        val roomId = UUID.randomUUID()
        val newPassword = "secretPassword"
        val request = UpdateGameRoomUseCase.Request(
            roomId = roomId,
            roomName = "공개->비공개",
            password = newPassword,
            maxUserCount = 6,
            bbAmount = 1000,
            sbAmount = 500
        )

        val commandSlot = slot<UpdateGameRoomCommand>()
        every { gameRoomService.updateRoom(capture(commandSlot)) } returns roomId

        // when
        val response = updateGameRoomUseCase.execute(request, executedAt)

        // then
        val capturedCommand = commandSlot.captured
        assertEquals(GameRoom.GameRoomAccess.Private(newPassword), capturedCommand.roomAccess)
        assertEquals(roomId, response.roomId)
    }

    @Test
    @DisplayName("비공개 방에서 공개 방으로 변경")
    fun `should change from private to public room`() {
        // given
        val roomId = UUID.randomUUID()
        val request = UpdateGameRoomUseCase.Request(
            roomId = roomId,
            roomName = "비공개->공개",
            password = null,
            maxUserCount = 6,
            bbAmount = 1000,
            sbAmount = 500
        )

        val commandSlot = slot<UpdateGameRoomCommand>()
        every { gameRoomService.updateRoom(capture(commandSlot)) } returns roomId

        // when
        val response = updateGameRoomUseCase.execute(request, executedAt)

        // then
        val capturedCommand = commandSlot.captured
        assertEquals(GameRoom.GameRoomAccess.Public, capturedCommand.roomAccess)
        assertEquals(roomId, response.roomId)
    }

    @Test
    @DisplayName("모든 요청 파라미터가 올바르게 전달되는지 확인")
    fun `should pass all request parameters correctly`() {
        // given
        val roomId = UUID.randomUUID()
        val request = UpdateGameRoomUseCase.Request(
            roomId = roomId,
            roomName = "완전한 수정 테스트 방",
            password = "testPassword",
            maxUserCount = 8,
            bbAmount = 3000,
            sbAmount = 1500
        )

        val commandSlot = slot<UpdateGameRoomCommand>()
        every { gameRoomService.updateRoom(capture(commandSlot)) } returns roomId

        // when
        val response = updateGameRoomUseCase.execute(request, executedAt)

        // then
        val capturedCommand = commandSlot.captured
        assertEquals(roomId, capturedCommand.roomId)
        assertEquals("완전한 수정 테스트 방", capturedCommand.roomName)
        assertEquals(GameRoom.GameRoomAccess.Private("testPassword"), capturedCommand.roomAccess)
        assertEquals(8, capturedCommand.maxPlayerCount)
        assertEquals(3000, capturedCommand.bbAmount)
        assertEquals(1500, capturedCommand.sbAmount)

        assertEquals(roomId, response.roomId)
    }

    @Test
    @DisplayName("서비스 메서드가 정확히 한 번 호출되는지 확인")
    fun `should call service updateRoom method exactly once`() {
        // given
        val roomId = UUID.randomUUID()
        val request = UpdateGameRoomUseCase.Request(
            roomId = roomId,
            roomName = "호출 테스트 방",
            password = null,
            maxUserCount = 6,
            bbAmount = 1000,
            sbAmount = 500
        )

        every { gameRoomService.updateRoom(any()) } returns roomId

        // when
        updateGameRoomUseCase.execute(request, executedAt)

        // then
        verify(exactly = 1) { gameRoomService.updateRoom(any()) }
    }

    @Test
    @DisplayName("Response의 roomId가 Service에서 반환된 값과 일치하는지 확인")
    fun `should return room id from service response`() {
        // given
        val roomId = UUID.randomUUID()
        val request = UpdateGameRoomUseCase.Request(
            roomId = roomId,
            roomName = "테스트 방",
            password = null,
            maxUserCount = 6,
            bbAmount = 1000,
            sbAmount = 500
        )

        every { gameRoomService.updateRoom(any()) } returns roomId

        // when
        val response = updateGameRoomUseCase.execute(request, executedAt)

        // then
        assertEquals(roomId, response.roomId)
    }
}
