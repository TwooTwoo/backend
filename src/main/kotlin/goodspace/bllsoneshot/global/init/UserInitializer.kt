package goodspace.bllsoneshot.global.init

import goodspace.bllsoneshot.entity.user.User
import goodspace.bllsoneshot.entity.user.UserRole
import goodspace.bllsoneshot.entity.user.UserRole.ROLE_MENTEE
import goodspace.bllsoneshot.entity.user.UserRole.ROLE_MENTOR
import goodspace.bllsoneshot.repository.user.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.core.io.ClassPathResource
import org.springframework.transaction.annotation.Transactional

@Component
class UserInitializer(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,

    @Value("\${init.mentor.login-id}")
    private val mentorLoginId: String,
    @Value("\${init.mentor.password}")
    private val mentorPassword: String,
    @Value("\${init.mentor.name}")
    private val mentorName: String,

    @Value("\${init.mentee1.login-id}")
    private val mentee1LoginId: String,
    @Value("\${init.mentee1.password}")
    private val mentee1Password: String,
    @Value("\${init.mentee1.name}")
    private val mentee1Name: String,

    @Value("\${init.mentee2.login-id}")
    private val mentee2LoginId: String,
    @Value("\${init.mentee2.password}")
    private val mentee2Password: String,
    @Value("\${init.mentee2.name}")
    private val mentee2Name: String
) : ApplicationRunner {

    @Transactional
    override fun run(args: ApplicationArguments) {
        val mentor = initIfNotExists(
            loginId = mentorLoginId,
            password = mentorPassword,
            name = mentorName,
            role = ROLE_MENTOR,
            profileImage = MENTOR_PROFILE_IMAGE
        )

        initIfNotExists(
            loginId = mentee1LoginId,
            password = mentee1Password,
            name = mentee1Name,
            role = ROLE_MENTEE,
            mentor = mentor,
            profileImage = MENTEE1_PROFILE_IMAGE
        )
        initIfNotExists(
            loginId = mentee2LoginId,
            password = mentee2Password,
            name = mentee2Name,
            role = ROLE_MENTEE,
            mentor = mentor,
            profileImage = MENTEE2_PROFILE_IMAGE
        )
    }

    private fun initIfNotExists(
        loginId: String,
        password: String,
        name: String,
        role: UserRole,
        mentor: User? = null,
        profileImage: ByteArray? = null
    ): User? {
        if (userRepository.existsByLoginId(loginId)) {
            return null
        }

        val user = User(
            loginId = loginId,
            password = passwordEncoder.encode(password)!!,
            role = role,
            name = name,
            mentor = mentor,
            profileImage = profileImage
        )

        return userRepository.save(user)
    }

    companion object {
        private val MENTOR_PROFILE_IMAGE: ByteArray by lazy { loadImage("professor.png") }
        private val MENTEE1_PROFILE_IMAGE: ByteArray by lazy { loadImage("studentA.png") }
        private val MENTEE2_PROFILE_IMAGE: ByteArray by lazy { loadImage("studentB.png") }

        private fun loadImage(filename: String): ByteArray =
            ClassPathResource(filename).inputStream.use { it.readBytes() }
    }
}
