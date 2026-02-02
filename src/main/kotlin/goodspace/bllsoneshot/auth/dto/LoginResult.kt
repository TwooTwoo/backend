package goodspace.bllsoneshot.auth.dto

import goodspace.bllsoneshot.entity.user.UserRole

data class LoginResult(
    val accessToken: String,
    val refreshToken: String,
    val role: UserRole
)
