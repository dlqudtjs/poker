package mong.poker.core.domain.user

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
    private val id: UUID,

    @Column(name = "nickname", nullable = false)
    private val nickname: String,

    @Column(name = "balance_amount", nullable = false)
    private val balanceAmount: Long,

    @Column(name = "created_at", nullable = false)
    private val createdAt: LocalDateTime,

    @Column(name = "updated_at")
    private val updatedAt: LocalDateTime?,

    @Column(name = "deleted_at")
    private val deletedAt: LocalDateTime?,
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

    fun getId(): UUID {
        return id
    }

    fun getNickname(): String {
        return nickname
    }
}
