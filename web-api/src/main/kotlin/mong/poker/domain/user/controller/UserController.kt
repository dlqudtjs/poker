package mong.poker.domain.user.controller

import jakarta.validation.Valid
import mong.poker.application.domain.user.usecase.SignUnUpUseCase
import mong.poker.domain.user.controller.request.SignUpRequest
import mong.poker.global.response.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.util.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val signInUpUseCase: SignUnUpUseCase
) {
    @PostMapping("/signup")
    fun signUp(
        @RequestBody @Valid request: SignUpRequest
    ): ResponseEntity<ApiResponse<UUID>> {
        val response = signInUpUseCase.execute(
            request = SignUnUpUseCase.Request(
                accountId = request.accountId,
                password = request.password,
                nickname = request.nickname,
            ),
            executedAt = LocalDateTime.now()
        )

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(response.id))
    }
}
