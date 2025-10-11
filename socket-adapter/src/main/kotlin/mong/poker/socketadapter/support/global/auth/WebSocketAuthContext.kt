package mong.poker.socketadapter.support.global.auth

import org.springframework.stereotype.Component
import java.util.*

@Component
class WebSocketAuthContext {
    private val threadLocal = ThreadLocal<UUID>()

    var id: UUID?
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
