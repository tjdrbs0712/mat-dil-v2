package hello.matdil.domain.review.controller;

import hello.matdil.auth.annotation.LoginUser;
import hello.matdil.auth.model.AuthUser;
import hello.matdil.domain.review.dto.*;
import hello.matdil.domain.review.facade.ReviewFacade;
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

    private final ReviewFacade reviewFacade;

    // 1. 리뷰 등록
    @PostMapping("/reviews")
    public ResponseEntity<SuccessResponse<ReviewResponseDto>> createReview(
            @LoginUser AuthUser loginUser,
            @RequestBody @Valid ReviewCreateRequestDto requestDto
    ) {
        ReviewResponseDto response = reviewFacade.createReview(
                loginUser.getUserId(), loginUser.getRole(), requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.success(response));
    }

    // 2. 리뷰 수정
    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<SuccessResponse<ReviewResponseDto>> updateReview(
            @LoginUser AuthUser loginUser,
            @PathVariable Long reviewId,
            @RequestBody @Valid ReviewUpdateRequestDto requestDto
    ) {
        ReviewResponseDto response = reviewFacade.updateReview(
                loginUser.getUserId(), loginUser.getRole(), reviewId, requestDto);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }

    // 3. 리뷰 조회 (슬라이스)
    @GetMapping("/stores/{storeId}/reviews")
    public ResponseEntity<SuccessResponse<SliceResponse<ReviewResponseDto, ReviewCursorResponseDto>>> getReviews(
            @PathVariable Long storeId,
            @ModelAttribute @Valid ReviewCursorRequestDto request
    ) {
        SliceResponse<ReviewResponseDto, ReviewCursorResponseDto> response = reviewFacade.getReviews(storeId, request);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }

    // 4. 리뷰 삭제
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<SuccessResponse<Void>> deleteReview(
            @LoginUser AuthUser authUser,
            @PathVariable Long reviewId
    ) {
        reviewFacade.deleteReview(authUser.getUserId(), authUser.getRole(), reviewId);
        return ResponseEntity.ok(SuccessResponse.success(null));
    }
}
