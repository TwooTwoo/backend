package goodspace.bllsoneshot.mentor.service

import goodspace.bllsoneshot.entity.assignment.CommentType
import goodspace.bllsoneshot.entity.assignment.RegisterStatus
import goodspace.bllsoneshot.global.exception.ExceptionMessage.USER_NOT_FOUND
import goodspace.bllsoneshot.mentor.dto.response.FeebackRequiredTaskSummaryResponse

import goodspace.bllsoneshot.mentor.dto.response.TaskUnfinishedSummaryResponse
import goodspace.bllsoneshot.repository.task.TaskRepository
import goodspace.bllsoneshot.repository.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate


@Service
class MentorDashboardService(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository
) {
    @Transactional(readOnly = true)
    fun getFeedbackRequiredTasks(
        mentorId: Long,
        date: LocalDate
    ): FeebackRequiredTaskSummaryResponse {
        userRepository.findById(mentorId)
            .orElseThrow { IllegalArgumentException(USER_NOT_FOUND.message) }

        val tasks = taskRepository.findFeedbackRequiredTasks(
            mentorId = mentorId,
            date = date,
            feedbackType = CommentType.FEEDBACK,
            registeredStatus = RegisterStatus.REGISTERED
        )

        return FeebackRequiredTaskSummaryResponse(
            taskCount = tasks.sumOf { it.submittedTaskCount },
            menteeNames = tasks.map { it.menteeName }.distinct()
        )
    }

    @Transactional(readOnly = true)
    fun getTaskUnfinishedMentees(
        mentorId: Long,
        date: LocalDate
    ): TaskUnfinishedSummaryResponse {

        val mentees = taskRepository.findTaskUnfinishedMentees(
            mentorId = mentorId,
            date = date
        )

        return TaskUnfinishedSummaryResponse(
            menteeCount = mentees.size,
            menteeNames = mentees.map { it.menteeName },
        )
    }
}
