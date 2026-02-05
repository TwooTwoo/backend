package goodspace.bllsoneshot.mentor.controller

import goodspace.bllsoneshot.global.security.userId
import goodspace.bllsoneshot.mentor.dto.response.FeebackRequiredTaskSummaryResponse
import goodspace.bllsoneshot.mentor.dto.response.TaskUnfinishedSummaryResponse
import goodspace.bllsoneshot.mentor.service.MentorDashboardService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import java.security.Principal
import java.time.LocalDate
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@PreAuthorize("hasRole('MENTOR')")
@RestController
@RequestMapping("/mentors/dashboard")
@Tag(name = "멘토 대시보드 API")
class MentorDashboardController(
    private val mentorDashboardService: MentorDashboardService
) {
    @GetMapping("/feedback-required-tasks")
    @Operation(
        summary = "피드백 작성이 필요한 항목 전체 조회",
        description = """
            멘토가 담당하는 멘티 중,
            오늘에 해당하는 과제 중 인증 사진을 제출했지만(ProofShot 존재)
            피드백이 등록되지 않은 과제만 집계합니다.
            
            [요청]
            date: 기준 날짜(yyyy-MM-dd)
            
            [응답]
            taskCount: 피드백 작성이 필요한 과제 건수
            menteeNames: 피드백 미작성 과제가 있는 멘티 이름 목록
        """
    )
    fun getFeedbackRequiredTasks(
        principal: Principal,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate
    ): ResponseEntity<FeebackRequiredTaskSummaryResponse> {
        val mentorId = principal.userId
        val response = mentorDashboardService.getFeedbackRequiredTasks(mentorId, date)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/unfinished-tasks")
    @Operation(
        summary = "학습 미이행(업로드 미제출) 멘티 전체 조회",
        description = """
            멘토가 담당하는 멘티 중,
            오늘에 해당하는 과제는 존재하지만 인증 사진을 아직 업로드하지 않은 멘티를 조회합니다.
            
            [요청]
            date: 기준 날짜(yyyy-MM-dd)
            
            [응답]
            menteeCount: 업로드 미제출 멘티 수
            menteeNames: 업로드 미제출 멘티 이름 목록
            mentees: 멘티 목록
              - menteeId: 멘티 ID
              - menteeName: 멘티 이름
        """
    )
    fun getTaskUnfinishedMentees(
        principal: Principal,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate
    ): ResponseEntity<TaskUnfinishedSummaryResponse> {
        val mentorId = principal.userId
        val response = mentorDashboardService.getTaskUnfinishedMentees(mentorId, date)
        return ResponseEntity.ok(response)
    }
}
