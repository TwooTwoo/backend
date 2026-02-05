package goodspace.bllsoneshot.report.service

import goodspace.bllsoneshot.entity.assignment.GeneralComment
import goodspace.bllsoneshot.entity.assignment.Subject
import goodspace.bllsoneshot.entity.user.LearningReport
import goodspace.bllsoneshot.entity.user.User
import goodspace.bllsoneshot.global.exception.ExceptionMessage
import goodspace.bllsoneshot.repository.user.LearningReportRepository
import goodspace.bllsoneshot.repository.user.UserRepository
import goodspace.bllsoneshot.report.dto.request.ReportCreateRequest
import goodspace.bllsoneshot.report.dto.response.ReportResponse
import goodspace.bllsoneshot.report.mapper.ReportMapper
import java.time.LocalDate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReportService(
    private val userRepository: UserRepository,
    private val learningReportRepository: LearningReportRepository,
    private val reportMapper: ReportMapper
) {

    @Transactional
    fun createLearningReport(mentorId: Long, menteeId: Long, request: ReportCreateRequest): ReportResponse {
        val mentee = userRepository.findById(menteeId)
            .orElseThrow { IllegalArgumentException(ExceptionMessage.USER_NOT_FOUND.message) }

        validateAssignedMentee(mentorId, mentee)
        validateReportDuplicate(menteeId, request.subject, request.startDate, request.endDate)

        val report = learningReportRepository.save(
            LearningReport(
                mentee = mentee,
                generalComment = GeneralComment(content = request.generalComment.trim()),
                subject = request.subject,
                startDate = request.startDate,
                endDate = request.endDate,
                goodPointContents = request.goodPoints,
                badPointContents = request.badPoints
            )
        )

        return reportMapper.map(report)
    }

    private fun validateAssignedMentee(
        mentorId: Long,
        mentee: User
    ) {
        check(mentee.mentor?.id == mentorId) { ExceptionMessage.MENTEE_ACCESS_DENIED.message }
    }

    private fun validateReportDuplicate(
        menteeId: Long,
        subject: Subject,
        startDate: LocalDate,
        endDate: LocalDate
    ) {
        val existsDuplicate = learningReportRepository.existsByMenteeIdAndSubjectAndStartDateAndEndDate(
            menteeId = menteeId,
            subject = subject,
            startDate = startDate,
            endDate = endDate
        )
        check(!existsDuplicate) { ExceptionMessage.REPORT_DUPLICATE.message }
    }
}
