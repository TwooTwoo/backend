package goodspace.bllsoneshot.mentor.dto.response

import goodspace.bllsoneshot.entity.assignment.Subject
import goodspace.bllsoneshot.task.dto.response.feedback.ProofShotResponse

data class MentorTaskDetailResponse(
    val taskId: Long,
    val taskName: String,
    val subject: Subject,
    val menteeName: String,

    val generalComment: String?,
    val hasProofShot: Boolean,
    val hasFeedback: Boolean,

    val proofShots: List<ProofShotResponse>
)
