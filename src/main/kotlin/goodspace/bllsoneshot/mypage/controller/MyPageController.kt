package goodspace.bllsoneshot.mypage.controller

import goodspace.bllsoneshot.entity.assignment.Subject
import goodspace.bllsoneshot.global.security.userId
import goodspace.bllsoneshot.mypage.dto.response.LearningHistoryResponse
import goodspace.bllsoneshot.mypage.dto.response.LearningStatusResponse
import goodspace.bllsoneshot.mypage.dto.response.UserInfoResponse
import goodspace.bllsoneshot.mypage.service.MyPageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import java.security.Principal
import java.time.LocalDate
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users/me")
@Tag(
    name = "마이 페이지 API"
)
class MyPageController(
    private val myPageService: MyPageService
) {

    @GetMapping
    @Operation(
        summary = "내 정보 조회",
        description = """
            본인의 정보를 조회합니다.
            
            [응답]
            name: 이름
            loginId: 로그인 아이디
            role: 사용자 역할 (ROLE_MENTOR, ROLE_MENTEE)
            grade: 학년 (멘티만 해당, 없으면 null)
            profileImage: 프로필 이미지 (Base64 인코딩, 없으면 null)
        """
    )
    fun getMyInfo(principal: Principal): ResponseEntity<UserInfoResponse> {
        val userId = principal.userId

        val response = myPageService.getMyInfo(userId)

        return ResponseEntity.ok(response)
    }

    @GetMapping("/learning-status")
    @Operation(
        summary = "전체 학습 현황 조회",
        description = """
            과목별로, '완료된 할 일'의 개수와 '할 일'의 개수를 조회합니다.
            
            [요청]
            date: 조회할 할 일의 날짜(yyyy-MM-dd)
            
            [응답]
            subject: 과목(KOREAN, ENGLISH, MATH)
            taskAmount: 해당 과목의 할 일 개수
            completedTaskAmount: 해당 과목의 완료된 할 일 개수
        """
    )
    fun getTotalLearningStatus(
        principal: Principal,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate
    ): ResponseEntity<List<LearningStatusResponse>> {
        val userId = principal.userId

        val response = myPageService.getTotalLearningStatus(userId, date)

        return ResponseEntity.ok(response)
    }

    @GetMapping("/learning-status/{subject}")
    @Operation(
        summary = "학습 현황 조회(단일 과목)",
        description = """
            해당 과목의 학습 현황(히스토리)을 조회합니다.
            
            [정렬]
            1. 완료되지 않은 일이며, 더 최근에 만들어진 할 일
            2. 완료되지 않은 일이며, 더 과거에 만들어진 할 일
            3. 완료된 일이며, 더 최근에 만들어진 할 일
            4. 완료된 일이며, 더 과거에 만들어진 할 일
            
            [요청]
            date: 조회할 할 일의 날짜(yyyy-MM-dd)
            subject: 과목(KOREAN, ENGLISH, MATH)
            
            [응답]
            todayTasks: 오늘(date)과 동일한 날짜의 할 일 목록
            historyTasks: date 이전의 할 일 목록
        """
    )
    fun getLearningStatusBySubject(
        principal: Principal,
        @PathVariable subject: Subject,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate
    ): ResponseEntity<LearningHistoryResponse> {
        val userId = principal.userId

        val response = myPageService.getLearningStatusBySubject(userId, subject, date)

        return ResponseEntity.ok(response)
    }
}
