package goodspace.bllsoneshot.auth.controller

import goodspace.bllsoneshot.auth.dto.LoginRequest
import goodspace.bllsoneshot.auth.dto.LoginResponse
import goodspace.bllsoneshot.auth.service.AuthService
import goodspace.bllsoneshot.global.cookie.RefreshTokenCookieProvider
import goodspace.bllsoneshot.global.security.TokenProvider
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
@Tag(
    name = "Auth API"
)
class AuthController(
    private val authService: AuthService,
    private val tokenProvider: TokenProvider,
    private val refreshTokenCookieProvider: RefreshTokenCookieProvider
) {

    @PostMapping("/login")
    @Operation(
        summary = "로그인",
        description = """
            아이디와 비밀번호를 기반으로 로그인합니다.
            리프레시 토큰은 쿠키에 저장됩니다.
            
            [요청]
            loginId: 로그인 아이디
            password: 비밀번호
            
            [응답]
            accessToken: 액세스 토큰
            role: 사용자 역할 (ROLE_MENTOR, ROLE_MENTEE)
        """
    )
    fun login(
        @Valid @RequestBody request: LoginRequest,
        response: HttpServletResponse
    ): ResponseEntity<LoginResponse> {
        val result = authService.login(request)

        refreshTokenCookieProvider.addToCookie(
            refreshToken = result.refreshToken,
            maxAgeSeconds = tokenProvider.getRefreshTokenValiditySeconds(),
            response = response
        )

        return ResponseEntity.ok(
            LoginResponse(
                accessToken = result.accessToken,
                role = result.role
            )
        )
    }

    @PostMapping("/refresh")
    @Operation(
        summary = "액세스 토큰 재발급",
        description = """
            쿠키에 저장된 리프레시 토큰으로 새 액세스 토큰을 발급합니다.
            로그인 시 설정된 refreshToken 쿠키가 요청에 포함되어야 합니다.
            
            [응답]
            accessToken: 새로 발급된 액세스 토큰
            role: 사용자 역할 (ROLE_MENTOR, ROLE_MENTEE)
        """
    )
    fun refresh(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ResponseEntity<LoginResponse> {
        val refreshToken = refreshTokenCookieProvider.extract(request)

        val result = authService.reissueAccessToken(refreshToken)

        refreshTokenCookieProvider.addToCookie(
            refreshToken = result.refreshToken,
            maxAgeSeconds = tokenProvider.getRefreshTokenValiditySeconds(),
            response = response
        )

        return ResponseEntity.ok(
            LoginResponse(
                accessToken = result.accessToken,
                role = result.role
            )
        )
    }
}
