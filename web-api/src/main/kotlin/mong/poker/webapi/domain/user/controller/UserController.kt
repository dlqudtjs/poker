package mong.poker.webapi.domain.user.controller

import jakarta.validation.Valid
import mong.poker.application.domain.user.usecase.GetMyProfileUseCase
import mong.poker.application.domain.user.usecase.SignInUseCase
import mong.poker.application.domain.user.usecase.SignUpUseCase
import mong.poker.webapi.domain.user.controller.request.SignInRequest
import mong.poker.webapi.domain.user.controller.request.SignUpRequest
import mong.poker.webapi.global.auth.ApiRequiredAuth
import mong.poker.webapi.global.response.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase,
) {
    @PostMapping("/signup")
    fun signUp(
        @RequestBody @Valid request: SignUpRequest
    ): ResponseEntity<ApiResponse<UUID>> {
        val response = signUpUseCase.execute(
            request = SignUpUseCase.Request(
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

    @PostMapping("/signin")
    fun signIn(
        @RequestBody @Valid request: SignInRequest
    ): ResponseEntity<ApiResponse<String>> {
        val response = signInUseCase.execute(
            request = SignInUseCase.Request(
                accountId = request.accountId,
                password = request.password,
            ),
            executedAt = LocalDateTime.now()
        )

        return ResponseEntity.ok(ApiResponse.success(response.token))
    }

    @GetMapping("/me")
    fun getMe(
        @ApiRequiredAuth id: UUID,
    ): ResponseEntity<ApiResponse<String>> {
        val response = getMyProfileUseCase.execute(
            request = GetMyProfileUseCase.Request(
                id = id
            ),
            executedAt = LocalDateTime.now()
        )

        return ResponseEntity.ok(ApiResponse.success(response.nickname))
    }
}
