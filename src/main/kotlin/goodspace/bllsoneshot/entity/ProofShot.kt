package goodspace.bllsoneshot.entity

import jakarta.persistence.*

@Entity
class ProofShot(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    val assignment: Assignment
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
