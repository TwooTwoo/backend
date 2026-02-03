package goodspace.bllsoneshot.entity.user

import goodspace.bllsoneshot.entity.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(
    @ManyToOne(fetch = FetchType.LAZY)
    val mentor: User? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: UserRole,
    @Column(nullable = false, unique = true)
    val loginId: String,
    @Column(nullable = false)
    val password: String,

    @Column(nullable = false)
    val name: String,
    val grade: String? = null
) : BaseEntity() {
    @OneToMany(mappedBy = "mentee", fetch = FetchType.LAZY)
    val subjects: MutableList<MenteeSubject> = mutableListOf()

    var refreshToken: String? = null
}
