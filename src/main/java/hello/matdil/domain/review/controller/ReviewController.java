package hello.matdil.domain.review.controller;

import hello.matdil.auth.annotation.LoginUser;
import hello.matdil.auth.model.AuthUser;
import hello.matdil.domain.review.dto.ReviewCreateRequestDto;
import hello.matdil.domain.review.dto.ReviewResponseDto;
import hello.matdil.domain.review.service.ReviewService;
import hello.matdil.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class ReviewController {

    private final ReviewService reviewService;

    // 1. 리뷰 등록
    @PostMapping("/reviews")
    public ResponseEntity<SuccessResponse<ReviewResponseDto>> createReview(
            @LoginUser AuthUser loginUser,
            @RequestBody @Valid ReviewCreateRequestDto requestDto
    ) {
        ReviewResponseDto response = reviewService.createReview(
                loginUser.getUserId(), loginUser.getRole(), requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.success(response));
    }

}
