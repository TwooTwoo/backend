package goodspace.bllsoneshot.task.mapper

import goodspace.bllsoneshot.entity.assignment.Comment
import goodspace.bllsoneshot.task.dto.response.feedback.QuestionResponse
import org.springframework.stereotype.Component

@Component
class QuestionMapper {

    fun map(comment: Comment): QuestionResponse {
        val annotation = comment.commentAnnotation

        return QuestionResponse(
            questionId = comment.id!!,
            questionNumber = annotation.number,
            content = comment.content,
            answer = comment.answer?.content,
            annotationId = annotation.id!!,
            annotationNumber = annotation.number,
            percentX = annotation.percentX,
            percentY = annotation.percentY
        )
    }
}
