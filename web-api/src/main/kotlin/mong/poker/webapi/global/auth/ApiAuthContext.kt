package mong.poker.webapi.global.auth

import mong.poker.core.domain.user.UserInfo
import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope
import java.util.*

@RequestScope
@Component
class ApiAuthContext {
    var userInfo: UserInfo? = null

    fun set(
        id: UUID?,
        nickname: String?,
    ) {
        if (id != null && nickname != null) {
            this.userInfo = UserInfo(
                id = id,
                nickname = nickname,
            )
        } else {
            this.userInfo = null
        }
    }

    fun clear() {
        this.userInfo = null
    }
}
