package mong.poker.domain.user.controller

import mong.poker.application.domain.user.usecase.user.SignUnUpUseCase
import mong.poker.global.response.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val signInUpUseCase: SignUnUpUseCase
) {

    @PostMapping("/signup")
    fun signUp(): ResponseEntity<ApiResponse<Nothing>> {
        signInUpUseCase.execute(
            request = SignUnUpUseCase.Request(
                nickname = "test"
            ),
            executedAt = java.time.LocalDateTime.now()
        )

        return ResponseEntity.ok(ApiResponse.success(null))
    }
}
