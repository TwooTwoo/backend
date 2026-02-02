package goodspace.bllsoneshot.entity

import jakarta.persistence.*

@Entity
class Planner(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    val mentee: User
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
