package mong.poker.core.domain.auth

import jakarta.persistence.*
import mong.poker.core.domain.user.User
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "password_accounts")
class PasswordAccount(

    @Id
    private val id: UUID,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private val user: User,

    @Column(name = "account_id", nullable = false)
    private val accountId: String,

    @Column(name = "password", nullable = false)
    private val password: String,

    @Column(name = "created_at", nullable = false)
    private val createdAt: LocalDateTime,

    @Column(name = "updated_at")
    private val updatedAt: LocalDateTime?,

    @Column(name = "deleted_at")
    private val deletedAt: LocalDateTime?,
) {
    companion object {
        fun create(
            user: User,
            accountId: String,
            password: String,
            createdAt: LocalDateTime,
        ) = PasswordAccount(
            id = UUID.randomUUID(),
            user = user,
            accountId = accountId,
            password = password,
            createdAt = createdAt,
            updatedAt = null,
            deletedAt = null,
        )
    }

    fun getPassword(): String {
        return password
    }

    fun getUser(): User {
        return user
    }
}
