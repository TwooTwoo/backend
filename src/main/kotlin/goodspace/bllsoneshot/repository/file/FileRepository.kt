package goodspace.bllsoneshot.repository.file

import goodspace.bllsoneshot.entity.assignment.File
import org.springframework.data.jpa.repository.JpaRepository

interface FileRepository : JpaRepository<File, Long> {
}