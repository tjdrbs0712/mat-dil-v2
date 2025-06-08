package hello.matdil.domain.review.controller;

import hello.matdil.auth.annotation.LoginUser;
import hello.matdil.auth.model.AuthUser;
import hello.matdil.domain.review.dto.*;
import hello.matdil.domain.review.facade.AdminReviewFacade;
import hello.matdil.global.response.SliceResponse;
import hello.matdil.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminReviewController {

    private final AdminReviewFacade adminReviewFacade;

    @PostMapping("/reviews")
    public ResponseEntity<SuccessResponse<ReviewResponseDto>> createReview(
            @LoginUser AuthUser authUser,
            @RequestBody @Valid AdminReviewCreateRequestDto dto
    ) {
        ReviewResponseDto response = adminReviewFacade.createReview(authUser.getRole(), dto);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }

    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<SuccessResponse<ReviewResponseDto>> updateReview(
            @LoginUser AuthUser authUser,
            @PathVariable Long reviewId,
            @RequestBody @Valid AdminReviewUpdateRequestDto requestDto
    ) {
        ReviewResponseDto response = adminReviewFacade.updateReview(authUser.getRole(), reviewId, requestDto);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<SuccessResponse<Void>> deleteReview(
            @LoginUser AuthUser authUser,
            @PathVariable Long reviewId) {
        adminReviewFacade.deleteReview(authUser.getRole(), reviewId);
        return ResponseEntity.ok(SuccessResponse.success(null));
    }

    @GetMapping("/stores/{storeId}/reviews")
    public ResponseEntity<SuccessResponse<SliceResponse<ReviewResponseDto, ReviewCursorResponseDto>>> getAllReviews(
            @LoginUser AuthUser authUser,
            @PathVariable Long storeId,
            @ModelAttribute @Valid ReviewCursorRequestDto request
    ) {
        SliceResponse<ReviewResponseDto, ReviewCursorResponseDto> response =
                adminReviewFacade.getAllReviews(authUser.getRole(), storeId, request);

        return ResponseEntity.ok(SuccessResponse.success(response));
    }
}

