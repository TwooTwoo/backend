package goodspace.bllsoneshot.entity.assignment

import goodspace.bllsoneshot.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Transient

@Entity
class ProofShot(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    val task: Task,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    val file: File
) : BaseEntity() {
    @OneToMany(mappedBy = "proofShot", fetch = FetchType.LAZY)
    val comments: MutableList<Comment> = mutableListOf()

    @get:Transient
    val questComments: List<Comment>
        get() = comments.filter { it.isQuestion }

    @get:Transient
    val registeredFeedbackComments: List<Comment>
        get() = comments.filter { it.isFeedback && it.isRegistered }
}
