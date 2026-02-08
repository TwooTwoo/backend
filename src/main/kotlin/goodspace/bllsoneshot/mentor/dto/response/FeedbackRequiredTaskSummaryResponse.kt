package goodspace.bllsoneshot.mentor.dto.response

data class FeedbackRequiredTaskSummaryResponse(
    val taskCount: Long,
    val menteeNames: List<String>
)
