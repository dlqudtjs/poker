package mong.poker.webapi.global.auth

import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope
import java.util.*

@RequestScope
@Component
class ApiAuthContext {
    var id: UUID? = null

    fun set(id: UUID?) {
        this.id = id
    }

    fun clear() {
        this.id = null
    }
}
