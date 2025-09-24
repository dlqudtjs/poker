package mong.poker.application.domain.auth.repository

import mong.poker.core.domain.auth.PasswordAccount
import org.springframework.data.jpa.repository.JpaRepository

interface PasswordAccountRepository : JpaRepository<PasswordAccount, Long> {
    fun findByAccountId(accountId: String): PasswordAccount?
    fun existsByAccountId(accountId: String): Boolean
}

