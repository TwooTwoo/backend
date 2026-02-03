package goodspace.bllsoneshot.task.mapper

import goodspace.bllsoneshot.entity.assignment.Comment
import goodspace.bllsoneshot.task.dto.response.feedback.FeedbackResponse
import org.springframework.stereotype.Component

@Component
class FeedbackMapper {

    fun map(comment: Comment): FeedbackResponse {
        val annotation = comment.commentAnnotation

        return FeedbackResponse(
            feedbackId = comment.id!!,
            feedbackNumber = annotation.number,
            content = comment.content,
            starred = comment.starred,
            registerStatus = comment.registerStatus,
            annotationId = annotation.id!!,
            annotationNumber = annotation.number,
            percentX = annotation.percentX,
            percentY = annotation.percentY
        )
    }
}
