package mong.poker.application.domain.room.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import mong.poker.application.domain.room.service.GameRoomService
import mong.poker.core.domain.room.GameRoom
import mong.poker.core.domain.room.command.CreateGameRoomCommand
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

@DisplayName("CreateGameRoomUseCase 테스트")
class CreateGameRoomUseCaseTest {

    private lateinit var gameRoomService: GameRoomService
    private lateinit var createGameRoomUseCase: CreateGameRoomUseCase
    private lateinit var executedAt: LocalDateTime

    @BeforeEach
    fun setUp() {
        gameRoomService = mockk()
        createGameRoomUseCase = CreateGameRoomUseCase(gameRoomService)
        executedAt = LocalDateTime.of(2025, 1, 1, 1, 0, 0)
    }

    @Test
    @DisplayName("공개 방 생성 - 비밀번호가 null인 경우")
    fun `should create public room when password is null`() {
        // given
        val hostUserId = UUID.randomUUID()
        val request = CreateGameRoomUseCase.Request(
            hostUserId = hostUserId,
            roomName = "테스트 방",
            password = null,
            maxUserCount = 6,
            bbAmount = 1000,
            sbAmount = 500
        )

        val commandSlot = slot<CreateGameRoomCommand>()
        every { gameRoomService.createRoom(capture(commandSlot)) } returns Unit

        // when
        val response = createGameRoomUseCase.execute(request, executedAt)

        // then
        verify(exactly = 1) { gameRoomService.createRoom(any()) }

        val capturedCommand = commandSlot.captured
        assertEquals(GameRoom.GameRoomAccess.Public, capturedCommand.roomAccess)
        assertNotNull(response.roomId)
    }

    @Test
    @DisplayName("공개 방 생성 - 비밀번호가 빈 문자열인 경우")
    fun `should create public room when password is empty`() {
        // given
        val hostUserId = UUID.randomUUID()
        val request = CreateGameRoomUseCase.Request(
            hostUserId = hostUserId,
            roomName = "공개 방",
            password = "",
            maxUserCount = 8,
            bbAmount = 2000,
            sbAmount = 1000
        )

        val commandSlot = slot<CreateGameRoomCommand>()
        every { gameRoomService.createRoom(capture(commandSlot)) } returns Unit

        // when
        val response = createGameRoomUseCase.execute(request, executedAt)

        // then
        verify(exactly = 1) { gameRoomService.createRoom(any()) }

        val capturedCommand = commandSlot.captured
        assertEquals(GameRoom.GameRoomAccess.Public, capturedCommand.roomAccess)
        assertNotNull(response.roomId)
    }

    @Test
    @DisplayName("공개 방 생성 - 비밀번호가 공백인 경우")
    fun `should create public room when password is blank`() {
        // given
        val hostUserId = UUID.randomUUID()
        val password = "  "
        val request = CreateGameRoomUseCase.Request(
            hostUserId = hostUserId,
            roomName = "공백 비밀번호 방",
            password = password,
            maxUserCount = 4,
            bbAmount = 500,
            sbAmount = 250
        )

        val commandSlot = slot<CreateGameRoomCommand>()
        every { gameRoomService.createRoom(capture(commandSlot)) } returns Unit

        // when
        val response = createGameRoomUseCase.execute(request, executedAt)

        // then
        verify(exactly = 1) { gameRoomService.createRoom(any()) }

        val capturedCommand = commandSlot.captured
        assertEquals(GameRoom.GameRoomAccess.Private(password), capturedCommand.roomAccess)
        assertNotNull(response.roomId)
    }

    @Test
    @DisplayName("비공개 방 생성 - 비밀번호가 있는 경우")
    fun `should create private room when password is provided`() {
        // given
        val hostUserId = UUID.randomUUID()
        val password = "secret123"
        val request = CreateGameRoomUseCase.Request(
            hostUserId = hostUserId,
            roomName = "비밀 방",
            password = password,
            maxUserCount = 2,
            bbAmount = 10000,
            sbAmount = 5000
        )

        val commandSlot = slot<CreateGameRoomCommand>()
        every { gameRoomService.createRoom(capture(commandSlot)) } returns Unit

        // when
        val response = createGameRoomUseCase.execute(request, executedAt)

        // then
        verify(exactly = 1) { gameRoomService.createRoom(any()) }

        val capturedCommand = commandSlot.captured
        assertEquals(GameRoom.GameRoomAccess.Private(password), capturedCommand.roomAccess)
        assertNotNull(response.roomId)
    }

    @Test
    @DisplayName("모든 요청 파라미터가 올바르게 전달되는지 확인")
    fun `should pass all request parameters correctly`() {
        // given
        val hostUserId = UUID.randomUUID()
        val password = "secret123"
        val request = CreateGameRoomUseCase.Request(
            hostUserId = hostUserId,
            roomName = "완전한 테스트 방",
            password = password,
            maxUserCount = 10,
            bbAmount = 5000,
            sbAmount = 2500
        )

        val commandSlot = slot<CreateGameRoomCommand>()
        every { gameRoomService.createRoom(capture(commandSlot)) } returns Unit

        // when
        val response = createGameRoomUseCase.execute(request, executedAt)

        // then
        val capturedCommand = commandSlot.captured
        assertEquals(request.roomName, capturedCommand.roomName)
        assertEquals(GameRoom.GameRoomAccess.Private(password), capturedCommand.roomAccess)
        assertEquals(request.maxUserCount, capturedCommand.maxPlayerCount)
        assertEquals(request.bbAmount, capturedCommand.bbAmount)
        assertEquals(request.sbAmount, capturedCommand.sbAmount)
        assertEquals(hostUserId, capturedCommand.hostUserId)

        assertNotNull(response.roomId)
    }
}
