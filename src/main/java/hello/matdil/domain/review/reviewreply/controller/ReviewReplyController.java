package hello.matdil.domain.review.reviewreply.controller;

import hello.matdil.auth.annotation.LoginUser;
import hello.matdil.auth.model.AuthUser;
import hello.matdil.domain.review.reviewreply.dto.ReviewReplyCreateRequestDto;
import hello.matdil.domain.review.reviewreply.dto.ReviewReplyResponseDto;
import hello.matdil.domain.review.reviewreply.dto.ReviewReplyUpdateRequestDto;
import hello.matdil.domain.review.reviewreply.facade.ReviewReplyFacade;
import hello.matdil.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/owner/reviews")
@PreAuthorize("hasRole('OWNER')")
public class ReviewReplyController {

    private final ReviewReplyFacade reviewReplyFacade;

    @PostMapping("/{reviewId}/reply")
    public ResponseEntity<SuccessResponse<ReviewReplyResponseDto>> createReply(
            @LoginUser AuthUser loginUser,
            @PathVariable Long reviewId,
            @RequestBody @Valid ReviewReplyCreateRequestDto requestDto
    ) {
        ReviewReplyResponseDto responseDto = reviewReplyFacade.createReply(loginUser.getUserId(), reviewId, requestDto);
        return ResponseEntity.ok(SuccessResponse.success(responseDto));
    }

    @PatchMapping("/reply/{replyId}")
    public ResponseEntity<SuccessResponse<ReviewReplyResponseDto>> updateReply(
            @LoginUser AuthUser loginUser,
            @PathVariable Long replyId,
            @RequestBody @Valid ReviewReplyUpdateRequestDto requestDto
    ) {
        ReviewReplyResponseDto responseDto = reviewReplyFacade.updateReply(loginUser.getUserId(), replyId, requestDto);
        return ResponseEntity.ok(SuccessResponse.success(responseDto));
    }
}

