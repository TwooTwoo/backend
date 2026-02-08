package goodspace.bllsoneshot.mentor.dto.response

data class MenteeManagementSummaryResponse(
    val totalMenteeCount: Int,
    val submittedMenteeCount: Int,
    val notSubmittedMenteeCount: Int,
    val mentees: List<MenteeManagementDetailResponse>
)
