package goodspace.bllsoneshot.entity

import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(
    @ManyToOne(fetch = FetchType.LAZY)
    val mentor: User? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
