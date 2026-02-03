package goodspace.bllsoneshot.repository.task

import goodspace.bllsoneshot.entity.assignment.ProofShot
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProofShotRepository : JpaRepository<ProofShot, Long> {

    @Query(
        """
        SELECT DISTINCT p FROM ProofShot p
        LEFT JOIN FETCH p.file
        LEFT JOIN FETCH p.comments c
        LEFT JOIN FETCH c.commentAnnotation
        LEFT JOIN FETCH c.answer
        WHERE p.task.id = :taskId
        """
    )
    fun findByTaskIdWithFileAndComments(taskId: Long): List<ProofShot>
}
