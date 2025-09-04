package mong.poker.application.global.support.usecase

import java.time.LocalDateTime

interface UseCase<T, R> {
    fun execute(
        request: T,
        executedAt: LocalDateTime,
    ): R
}
