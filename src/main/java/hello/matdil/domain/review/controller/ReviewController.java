package hello.matdil.domain.review.controller;

import hello.matdil.auth.annotation.LoginUser;
import hello.matdil.auth.model.AuthUser;
import hello.matdil.domain.review.dto.*;
import hello.matdil.domain.review.service.ReviewService;
import hello.matdil.global.response.SliceResponse;
import hello.matdil.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<SuccessResponse<ReviewResponseDto>> updateReview(
            @LoginUser AuthUser loginUser,
            @PathVariable Long reviewId,
            @RequestBody @Valid ReviewUpdateRequestDto requestDto
    ) {
        ReviewResponseDto response = reviewService.updateReview(
                loginUser.getUserId(), loginUser.getRole(), reviewId, requestDto);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }

    @GetMapping("/stores/{storeId}/reviews")
    public ResponseEntity<SuccessResponse<SliceResponse<ReviewResponseDto, ReviewCursorResponseDto>>> getReviews(
            @LoginUser(required = false) AuthUser authUser,
            @PathVariable Long storeId,
            @ModelAttribute @Valid ReviewCursorRequestDto request
    ) {
        SliceResponse<ReviewResponseDto, ReviewCursorResponseDto> response = reviewService.getReviews(
                authUser.getUserId(), authUser.getRole(), storeId, request
        );
        return ResponseEntity.ok(SuccessResponse.success(response));
    }



}
