package goodspace.bllsoneshot.entity.assignment

import goodspace.bllsoneshot.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne

@Entity
class Answer(
    @OneToOne(mappedBy = "answer", fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    val question: Comment,

    @Column(nullable = false)
    val content: String
) : BaseEntity()
