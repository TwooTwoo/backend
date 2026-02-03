package goodspace.bllsoneshot.task.service

import goodspace.bllsoneshot.entity.assignment.ColumnLink
import goodspace.bllsoneshot.entity.assignment.Task
import goodspace.bllsoneshot.entity.assignment.Worksheet
import goodspace.bllsoneshot.entity.user.User
import goodspace.bllsoneshot.entity.user.UserRole
import goodspace.bllsoneshot.global.exception.ExceptionMessage.*
import goodspace.bllsoneshot.repository.file.FileRepository
import goodspace.bllsoneshot.repository.task.ProofShotRepository
import goodspace.bllsoneshot.repository.task.TaskRepository
import goodspace.bllsoneshot.repository.user.UserRepository
import goodspace.bllsoneshot.task.dto.request.MenteeTaskCreateRequest
import goodspace.bllsoneshot.task.dto.request.MentorTaskCreateRequest
import goodspace.bllsoneshot.task.dto.request.TaskCompleteUpdateRequest
import goodspace.bllsoneshot.task.dto.response.TaskFeedbackResponse
import goodspace.bllsoneshot.task.dto.response.TaskResponse
import goodspace.bllsoneshot.task.mapper.TaskFeedbackMapper
import goodspace.bllsoneshot.task.mapper.TaskMapper
import jdk.internal.agent.AgentConfigurationError.FILE_NOT_FOUND
import java.time.LocalDate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val proofShotRepository: ProofShotRepository,
    private val userRepository: UserRepository,
    private val taskMapper: TaskMapper,
    private val taskFeedbackMapper: TaskFeedbackMapper,
    private val fileRepository: FileRepository
) {

    fun findTasksByDate(
        userId: Long,
        date: LocalDate
    ): List<TaskResponse> {
        val tasks = taskRepository.findByMenteeIdAndDate(userId, date)

        return taskMapper.map(tasks)
    }

    @Transactional
    fun createTaskByMentor(mentorId: Long, request: MentorTaskCreateRequest): TaskResponse {
        val mentee: User = userRepository.findById(request.menteeId)
            .orElseThrow() { IllegalArgumentException(USER_NOT_FOUND.message) }

        val mentor: User = userRepository.findById(mentorId)
            .orElseThrow() { IllegalArgumentException(USER_NOT_FOUND.message) }

        val task = Task(
            mentee = mentee,
            mentor = mentor,
            name = request.taskName,
            startDate = request.date,
            dueDate = request.date,
            goalMinutes = request.goalMinutes,
            actualMinutes = null,
            subject = request.subject,
            createdBy = UserRole.ROLE_MENTOR
        )
        task.worksheets.addAll(
            request.worksheets
                .mapNotNull { it.fileId }
                .map { fileId -> Worksheet(task, fileRepository.findById(fileId).orElseThrow({ IllegalArgumentException(FILE_NOT_FOUND)})) }
        )
        task.columnLinks.addAll(
            request.columnLinks
                .mapNotNull { it.link?.takeIf { link -> link.isNotBlank()} }
                .map { link -> ColumnLink(task, link) }
        )

        val savedTask = taskRepository.save(task)

        return taskMapper.map(savedTask)
    }

    @Transactional
    fun createTaskByMentee(userId: Long, request: MenteeTaskCreateRequest): TaskResponse {
        val mentee = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException(USER_NOT_FOUND.message) }

        val task = Task(
            mentee = mentee,
            mentor = null,
            name = request.taskName,
            startDate = request.date,
            dueDate = request.date,
            goalMinutes = request.goalMinutes,
            actualMinutes = null,
            subject = request.subject,
            createdBy = UserRole.ROLE_MENTEE
        )

        val savedTask = taskRepository.save(task)

        return taskMapper.map(savedTask)
    }

    @Transactional
    fun getTaskFeedback(userId: Long, taskId: Long): TaskFeedbackResponse {
        val task = taskRepository.findByIdWithMenteeAndGeneralCommentAndProofShots(taskId)
            ?: throw IllegalArgumentException(TASK_NOT_FOUND.message)

        validateTaskOwnership(task, userId)
        validateHasFeedback(task)

        task.markFeedbackAsRead()

        return taskFeedbackMapper.map(task)
    }

    @Transactional
    fun updateCompleted(
        userId: Long,
        taskId: Long,
        request: TaskCompleteUpdateRequest
    ) {
        val task = findTaskBy(taskId)

        validateTaskOwnership(task, userId)

        task.completed = request.completed
    }

    private fun findTaskBy(taskId: Long): Task {
        return taskRepository.findById(taskId)
            .orElseThrow { IllegalArgumentException(TASK_NOT_FOUND.message) }
    }

    private fun validateTaskOwnership(
        task: Task,
        menteeId: Long
    ) {
        check(task.mentee.id == menteeId) { TASK_ACCESS_DENIED.message }
    }

    private fun validateHasFeedback(task: Task) {
        check(task.hasFeedback()) { FEEDBACK_NOT_FOUND.message }
    }
}
