package hello.matdil.domain.review.dto;

import hello.matdil.domain.review.entity.Review;
import hello.matdil.domain.review.entity.ReviewImage;
import hello.matdil.domain.review.reviewreply.dto.ReviewReplyResponseDto;
import hello.matdil.domain.review.reviewreply.entity.ReviewReply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponseDto {

    private Long id;
    private Long userId;
    private Long orderId;
    private Long storeId;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> imageUrls;
    private ReviewReplyResponseDto reply;

    public static ReviewResponseDto from(Review review) {
        return ReviewResponseDto.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .orderId(review.getOrderId())
                .storeId(review.getStoreId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .imageUrls(review.getImages().stream()
                        .map(ReviewImage::getImageUrl)
                        .toList())
                .build();
    }

    public static ReviewResponseDto from(Review review, ReviewReply reply) {
        return ReviewResponseDto.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .orderId(review.getOrderId())
                .storeId(review.getStoreId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .imageUrls(review.getImages() != null ?
                        review.getImages().stream()
                                .map(ReviewImage::getImageUrl)
                                .toList()
                        : List.of())
                .reply(reply != null ? ReviewReplyResponseDto.from(reply) : null)
                .build();
    }
}

