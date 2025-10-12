package mong.poker.webapi.domain.user.controller

import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import mong.poker.application.domain.user.usecase.GetMyProfileUseCase
import mong.poker.application.domain.user.usecase.SignInUseCase
import mong.poker.application.domain.user.usecase.SignUpUseCase
import mong.poker.core.domain.user.UserInfo
import mong.poker.webapi.domain.user.controller.request.SignInRequest
import mong.poker.webapi.domain.user.controller.request.SignUpRequest
import mong.poker.webapi.global.auth.ApiRequiredAuth
import mong.poker.webapi.global.response.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase,
) {
    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    fun signUp(
        @RequestBody @Valid request: SignUpRequest
    ): ResponseEntity<ApiResponse<SignUpUseCase.Response>> {
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
            .body(ApiResponse.success(response))
    }

    @Operation(summary = "로그인")
    @PostMapping("/signin")
    fun signIn(
        @RequestBody @Valid request: SignInRequest
    ): ResponseEntity<ApiResponse<SignInUseCase.Response>> {
        val response = signInUseCase.execute(
            request = SignInUseCase.Request(
                accountId = request.accountId,
                password = request.password,
            ),
            executedAt = LocalDateTime.now()
        )

        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @Operation(summary = "내 프로필 조회")
    @GetMapping("/me")
    fun getMe(
        @ApiRequiredAuth userInfo: UserInfo,
    ): ResponseEntity<ApiResponse<GetMyProfileUseCase.Response>> {
        val response = getMyProfileUseCase.execute(
            request = GetMyProfileUseCase.Request(
                userId = userInfo.id
            ),
            executedAt = LocalDateTime.now()
        )

        return ResponseEntity.ok(ApiResponse.success(response))
    }
}
