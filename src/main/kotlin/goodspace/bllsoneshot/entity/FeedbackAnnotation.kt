package goodspace.bllsoneshot.entity

import jakarta.persistence.*

@Entity
class FeedbackAnnotation(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    val feedback: Feedback,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    val proofShot: ProofShot
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
