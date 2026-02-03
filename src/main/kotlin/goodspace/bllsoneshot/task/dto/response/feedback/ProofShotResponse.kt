package goodspace.bllsoneshot.task.dto.response.feedback

data class ProofShotResponse(
    val proofShotId: Long,
    val imageFileId: Long,

    val questions: List<QuestionResponse>,
    val feedbacks: List<FeedbackResponse>
)
