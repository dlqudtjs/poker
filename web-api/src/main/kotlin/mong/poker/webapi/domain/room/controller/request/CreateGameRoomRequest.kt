package mong.poker.webapi.domain.room.controller.request

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateGameRoomRequest(
    @field:NotBlank(message = "방 제목은 필수값 입니다.")
    val title: String,
    val password: String?,
    @field:Max(8, message = "최대 인원은 8명입니다.")
    val maxCapacity: Int,
    @field:NotNull(message = "블라인드 금액은 필수값 입니다.")
    val bbAmount: Int,
    @field:NotNull(message = "스몰 블라인드는 필수값 입니다.")
    val sbAmount: Int,
    @field:Min(1, message = "총 라운드는 최소 1 이상이어야 합니다.")
    val totalRounds: Int,
)
