package goodspace.bllsoneshot.task.dto.response

data class ProofShotResponse(
    val proofShotId: Long,
    val imageFileId: Long,

    val questions: List<QuestionResponse>,
    val questionAnnotations: List<CommentAnnotationResponse>,

    val feedbacks: List<FeedbackResponse>,
    val feedbackAnnotations: List<CommentAnnotationResponse>
)
