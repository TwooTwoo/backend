package goodspace.bllsoneshot.repository.user

import goodspace.bllsoneshot.entity.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<User, Long> {
    fun existsByLoginId(loginId: String): Boolean
    fun findByLoginId(loginId: String): User?

    @Query(
        """
        SELECT DISTINCT u FROM User u
        LEFT JOIN FETCH u.subjects
        WHERE u.mentor.id = :mentorId
        """
    )
    fun findMenteesWithSubjectsByMentorId(mentorId: Long): List<User>
}
