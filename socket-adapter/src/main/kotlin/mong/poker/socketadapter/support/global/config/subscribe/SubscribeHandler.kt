package mong.poker.socketadapter.support.global.config.subscribe

import mong.poker.core.domain.user.UserInfo

interface SubscribeHandler {
    /**
     * 처리 가능한 destination 인지 확인
     */
    fun supports(destination: String): Boolean

    /**
     * 구독 처리 로직
     */
    fun handleSubscribe(destination: String, userInfo: UserInfo)
}
