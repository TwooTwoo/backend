package goodspace.bllsoneshot.notification.dto.request

import jakarta.validation.constraints.NotBlank

data class FcmTokenRequest(
    @field:NotBlank(message = "FCM 토큰은 필수입니다.")
    val token: String
)
