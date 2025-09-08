package mong.poker.core.domain.auth.domain

import jakarta.persistence.*
import mong.poker.core.domain.user.domain.User
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "password_accounts")
class PasswordAccount(

    @Id
    val id: UUID,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(name = "account_id", nullable = false)
    val accountId: String,

    @Column(name = "password", nullable = false)
    val password: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at")
    val updatedAt: LocalDateTime?,

    @Column(name = "deleted_at")
    val deletedAt: LocalDateTime?,
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
}
