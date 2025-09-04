package mong.poker.core.domain.user.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "users")
class User(

    @Id
    val id: UUID,

    @Column(name = "ninkname", nullable = false)
    val nickname: String,

    @Column(name = "balance_amount", nullable = false)
    val balanceAmount: Long,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at")
    val updatedAt: LocalDateTime?,

    @Column(name = "deleted_at")
    val deletedAt: LocalDateTime?,
) {
    companion object {
        fun create(
            nickname: String,
            balanceAmount: Long = 0,
            createdAt: LocalDateTime,
        ) = User(
            id = UUID.randomUUID(),
            nickname = nickname,
            balanceAmount = balanceAmount,
            createdAt = createdAt,
            updatedAt = null,
            deletedAt = null,
        )
    }
}
