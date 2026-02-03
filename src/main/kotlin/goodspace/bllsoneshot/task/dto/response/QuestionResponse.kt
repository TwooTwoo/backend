package goodspace.bllsoneshot.task.dto.response

data class QuestionResponse(
    val questionId: Long,
    val questionNumber: Int,

    val content: String,
    val answer: String?
)
