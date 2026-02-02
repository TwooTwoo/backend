package goodspace.bllsoneshot.auth.dto

import goodspace.bllsoneshot.entity.user.UserRole

data class LoginResponse(
    val accessToken: String,
    val role: UserRole
)
