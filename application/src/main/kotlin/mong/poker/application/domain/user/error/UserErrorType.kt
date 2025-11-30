package mong.poker.application.domain.user.error

import mong.poker.core.exception.ErrorType

enum class UserErrorType(
    override val status: Int,
    override val message: String
) : ErrorType {
    DUPLICATED_NICKNAME(400, "닉네임이 이미 존재합니다."),
    DUPLICATED_ACCOUNT_ID(400, "계정 ID가 이미 존재합니다."),
    INVALID_LOGIN_INFO(400, "로그인 정보가 올바르지 않습니다."),
    USER_NOT_FOUND(400, "유저를 찾을 수 없습니다."),
}
