package goodspace.bllsoneshot.global.file

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.util.UUID

@Service
class FileStorageService(
    private val s3Client: S3Client,
    @Value("\${aws.s3.bucket}") private val bucket: String
) {
    fun uploadWorksheet(file: MultipartFile): UploadedFile {
        val objectKey = "worksheets/${UUID.randomUUID()}_${file.originalFilename}"
        val request = PutObjectRequest.builder()
            .bucket(bucket)
            .key(objectKey)
            .contentType(file.contentType ?: "application/octet-stream")
            .contentLength(file.size)
            .build()

        s3Client.putObject(request, RequestBody.fromBytes(file.bytes))

        return UploadedFile(
            objectKey = objectKey,
            fileName = file.originalFilename ?: "file",
            contentType = file.contentType ?: "application/octet-stream",
            byteSize = file.size,
            bucketName = bucket
        )
    }
}

data class UploadedFile(
    val objectKey: String,
    val fileName: String,
    val contentType: String,
    val byteSize: Long,
    val bucketName: String
)