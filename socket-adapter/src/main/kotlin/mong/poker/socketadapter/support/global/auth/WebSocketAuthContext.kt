package mong.poker.socketadapter.support.global.auth

import mong.poker.core.domain.user.UserInfo
import org.springframework.stereotype.Component

@Component
class WebSocketAuthContext {
    private val threadLocal = ThreadLocal<UserInfo>()

    var userInfo: UserInfo?
        get() = threadLocal.get()
        set(value) {
            if (value != null) {
                threadLocal.set(value)
            } else {
                threadLocal.remove()
            }
        }

    fun clear() {
        threadLocal.remove()
    }
}
