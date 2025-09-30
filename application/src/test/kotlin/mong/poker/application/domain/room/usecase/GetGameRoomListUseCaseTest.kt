package mong.poker.application.domain.room.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import mong.poker.application.domain.room.service.GameRoomService
import mong.poker.core.domain.room.GameRoom
import mong.poker.core.domain.room.GameRoomStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

@DisplayName("GetGameRoomListUseCase 테스트")
class GetGameRoomListUseCaseTest {

    private lateinit var gameRoomService: GameRoomService
    private lateinit var getGameRoomListUseCase: GetGameRoomListUseCase
    private lateinit var executedAt: LocalDateTime

    @BeforeEach
    fun setUp() {
        gameRoomService = mockk()
        getGameRoomListUseCase = GetGameRoomListUseCase(gameRoomService)
        executedAt = LocalDateTime.of(2025, 1, 1, 1, 0, 0)
    }

    @Test
    @DisplayName("빈 방 목록을 반환하는 경우")
    fun `should return empty room list when no rooms exist`() {
        // given
        every { gameRoomService.getAllRooms() } returns emptyList()

        // when
        val response = getGameRoomListUseCase.execute(Unit, executedAt)

        // then
        verify(exactly = 1) { gameRoomService.getAllRooms() }
        assertEquals(0, response.rooms.size)
        assertTrue(response.rooms.isEmpty())
    }

    @Test
    @DisplayName("공개 방 하나를 반환하는 경우")
    fun `should return single public room`() {
        // given
        val roomId = UUID.randomUUID()
        val mockRoom = createMockGameRoom(
            id = roomId,
            roomName = "공개 테스트 방",
            maxPlayerCount = 6,
            isPrivate = false,
            bbAmount = 1000,
            sbAmount = 500
        )

        every { gameRoomService.getAllRooms() } returns listOf(mockRoom)

        // when
        val response = getGameRoomListUseCase.execute(Unit, executedAt)

        // then
        verify(exactly = 1) { gameRoomService.getAllRooms() }
        assertEquals(1, response.rooms.size)

        val roomInfo = response.rooms.first()
        assertEquals(roomId, roomInfo.roomId)
        assertEquals("공개 테스트 방", roomInfo.roomName)
        assertEquals(6, roomInfo.maxUserCount)
        assertFalse(roomInfo.isPrivate)
        assertEquals(1000, roomInfo.bbAmount)
        assertEquals(500, roomInfo.sbAmount)
    }

    @Test
    @DisplayName("비공개 방 하나를 반환하는 경우")
    fun `should return single private room`() {
        // given
        val roomId = UUID.randomUUID()
        val mockRoom = createMockGameRoom(
            id = roomId,
            roomName = "비밀 방",
            maxPlayerCount = 4,
            isPrivate = true,
            bbAmount = 5000,
            sbAmount = 2500
        )

        every { gameRoomService.getAllRooms() } returns listOf(mockRoom)

        // when
        val response = getGameRoomListUseCase.execute(Unit, executedAt)

        // then
        verify(exactly = 1) { gameRoomService.getAllRooms() }
        assertEquals(1, response.rooms.size)

        val roomInfo = response.rooms.first()
        assertEquals(roomId, roomInfo.roomId)
        assertEquals("비밀 방", roomInfo.roomName)
        assertEquals(4, roomInfo.maxUserCount)
        assertTrue(roomInfo.isPrivate)
        assertEquals(5000, roomInfo.bbAmount)
        assertEquals(2500, roomInfo.sbAmount)
    }

    @Test
    @DisplayName("여러 개의 방을 반환하는 경우")
    fun `should return multiple rooms with different properties`() {
        // given
        val room1Id = UUID.randomUUID()
        val room2Id = UUID.randomUUID()
        val room3Id = UUID.randomUUID()

        val mockRoom1 = createMockGameRoom(
            id = room1Id,
            roomName = "초보자 방",
            maxPlayerCount = 8,
            isPrivate = false,
            bbAmount = 100,
            sbAmount = 50
        )

        val mockRoom2 = createMockGameRoom(
            id = room2Id,
            roomName = "고수 방",
            maxPlayerCount = 2,
            isPrivate = true,
            bbAmount = 10000,
            sbAmount = 5000
        )

        val mockRoom3 = createMockGameRoom(
            id = room3Id,
            roomName = "일반 방",
            maxPlayerCount = 6,
            isPrivate = false,
            bbAmount = 1000,
            sbAmount = 500
        )

        every { gameRoomService.getAllRooms() } returns listOf(mockRoom1, mockRoom2, mockRoom3)

        // when
        val response = getGameRoomListUseCase.execute(Unit, executedAt)

        // then
        verify(exactly = 1) { gameRoomService.getAllRooms() }
        assertEquals(3, response.rooms.size)

        // 첫 번째 방 검증
        val firstRoom = response.rooms[0]
        assertEquals(room1Id, firstRoom.roomId)
        assertEquals("초보자 방", firstRoom.roomName)
        assertEquals(8, firstRoom.maxUserCount)
        assertFalse(firstRoom.isPrivate)
        assertEquals(100, firstRoom.bbAmount)
        assertEquals(50, firstRoom.sbAmount)

        // 두 번째 방 검증
        val secondRoom = response.rooms[1]
        assertEquals(room2Id, secondRoom.roomId)
        assertEquals("고수 방", secondRoom.roomName)
        assertEquals(2, secondRoom.maxUserCount)
        assertTrue(secondRoom.isPrivate)
        assertEquals(10000, secondRoom.bbAmount)
        assertEquals(5000, secondRoom.sbAmount)

        // 세 번째 방 검증
        val thirdRoom = response.rooms[2]
        assertEquals(room3Id, thirdRoom.roomId)
        assertEquals("일반 방", thirdRoom.roomName)
        assertEquals(6, thirdRoom.maxUserCount)
        assertFalse(thirdRoom.isPrivate)
        assertEquals(1000, thirdRoom.bbAmount)
        assertEquals(500, thirdRoom.sbAmount)
    }

    @Test
    @DisplayName("서비스 메서드가 정확히 한 번 호출되는지 확인")
    fun `should call service getAllRooms method exactly once`() {
        // given
        every { gameRoomService.getAllRooms() } returns emptyList()

        // when
        getGameRoomListUseCase.execute(Unit, executedAt)

        // then
        verify(exactly = 1) { gameRoomService.getAllRooms() }
    }

    // Mock 객체 생성을 위한 헬퍼 메서드
    private fun createMockGameRoom(
        id: UUID,
        roomName: String,
        maxPlayerCount: Int,
        isPrivate: Boolean,
        bbAmount: Int,
        sbAmount: Int
    ): GameRoom {
        val mockRoom = mockk<GameRoom>()
        val mockGameRoomStatus = mockk<GameRoomStatus>()
        val mockRoomAccess = mockk<GameRoom.GameRoomAccess>()

        every { mockRoom.id } returns id
        every { mockRoom.getRoomName() } returns roomName
        every { mockRoom.getGameRoomStatus() } returns mockGameRoomStatus
        every { mockRoom.getRoomAccess() } returns mockRoomAccess

        every { mockGameRoomStatus.getMaxPlayerCount() } returns maxPlayerCount
        every { mockGameRoomStatus.getBbAmount() } returns bbAmount
        every { mockGameRoomStatus.getSbAmount() } returns sbAmount

        every { mockRoomAccess.isPrivate() } returns isPrivate

        return mockRoom
    }
}
