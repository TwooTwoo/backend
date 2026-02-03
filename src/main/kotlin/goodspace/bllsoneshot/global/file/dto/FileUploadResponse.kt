package goodspace.bllsoneshot.global.file.dto

data class FileUploadResponse(
    val fileId: Long,
    val url: String,
    val originalName: String,
    val contentType: String
)