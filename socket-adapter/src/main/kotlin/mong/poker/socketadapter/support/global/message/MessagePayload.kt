package mong.poker.socketadapter.support.global.message

import mong.poker.core.domain.user.UserInfo
import java.time.LocalDateTime

data class MessagePayload(
    val senderInfo: UserInfo,
    val message: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
