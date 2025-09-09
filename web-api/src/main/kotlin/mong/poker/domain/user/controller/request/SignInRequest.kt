package mong.poker.domain.user.controller.request

import jakarta.validation.constraints.NotBlank

data class SignInRequest(
    @field:NotBlank(message = "아이디는 필수 입력값입니다.")
    val accountId: String,
    @field:NotBlank(message = "비밀번호는 필수 입력값입니다.")
    val password: String,
)
