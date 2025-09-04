package mong.poker.domain.user.controller

import mong.poker.global.response.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController {

    @PostMapping("/signup")
    fun signUp(): ResponseEntity<ApiResponse<Nothing>> {
        return "sign up"
    }
}
