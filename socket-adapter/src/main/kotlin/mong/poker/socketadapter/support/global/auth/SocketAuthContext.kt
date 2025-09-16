package mong.poker.socketadapter.support.global.auth

import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope

@RequestScope
@Component
class SocketAuthContext {
    var id: String? = null

    fun set(id: String?) {
        this.id = id
    }

    fun clear() {
        this.id = null
    }
}
