package goodspace.bllsoneshot.mentor.dto.response

data class FeedbackRequiredTaskResponse(
    val menteeId: Long,
    val menteeName: String,
    val submittedTaskCount: Long
)
