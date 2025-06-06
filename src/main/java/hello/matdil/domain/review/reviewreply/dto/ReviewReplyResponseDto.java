package hello.matdil.domain.review.reviewreply.dto;

import hello.matdil.domain.review.reviewreply.entity.ReviewReply;

import java.time.LocalDateTime;

public record ReviewReplyResponseDto(
        Long id,
        Long reviewId,
        Long ownerId,
        String replyText,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ReviewReplyResponseDto from(ReviewReply reply) {
        return new ReviewReplyResponseDto(
                reply.getId(),
                reply.getReview().getId(),
                reply.getOwnerId(),
                reply.getReplyText(),
                reply.getCreatedAt(),
                reply.getUpdatedAt()
        );
    }
}

