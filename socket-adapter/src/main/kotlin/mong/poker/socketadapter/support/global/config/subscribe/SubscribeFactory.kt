package mong.poker.socketadapter.support.global.config.subscribe

import org.springframework.stereotype.Component

@Component
class SubscribeFactory(
    private val subscribeHandlers: List<SubscribeHandler>
) {
    fun getHandler(destination: String): SubscribeHandler? {
        return subscribeHandlers.firstOrNull { it.supports(destination) }
    }
}
