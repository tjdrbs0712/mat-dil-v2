package hello.matdil.domain.review.factory;

import hello.matdil.domain.review.dto.ReviewCreateRequestDto;
import hello.matdil.domain.review.entity.Review;
import hello.matdil.domain.review.entity.ReviewImage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewFactory {

    public Review create(Long userId, ReviewCreateRequestDto dto) {

        Review review = Review.builder()
                .userId(userId)
                .orderId(dto.orderId())
                .storeId(dto.storeId())
                .rating(dto.rating())
                .comment(dto.comment())
                .build();

        List<ReviewImage> images = dto.imageUrls().stream()
                .map(url -> ReviewImage.create(url, review))
                .toList();


        review.getImages().addAll(images);

        return review;
    }
}
