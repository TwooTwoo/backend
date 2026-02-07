package goodspace.bllsoneshot.mentor.dto.response

import goodspace.bllsoneshot.entity.assignment.Subject
import java.time.LocalDate

data class MenteeManagementDetailResponse(
    val menteeId: Long,
    val menteeName: String,
    val grade: String?,
    val subjects: List<Subject>,
    val recentTaskDate: LocalDate?,
    val recentTaskName: String?,
    val submitted: Boolean
)
