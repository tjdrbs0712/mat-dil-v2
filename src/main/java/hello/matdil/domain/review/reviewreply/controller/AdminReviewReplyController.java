package hello.matdil.domain.review.reviewreply.controller;

import hello.matdil.domain.review.reviewreply.facade.AdminReviewReplyFacade;
import hello.matdil.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/admin/reviews")
@PreAuthorize("hasRole('ADMIN')")
public class AdminReviewReplyController {
    private final AdminReviewReplyFacade adminReviewReplyFacade;

    @DeleteMapping("/reply/{replyId}")
    public ResponseEntity<SuccessResponse<Void>> deleteReply(
            @PathVariable Long replyId
    ) {
        adminReviewReplyFacade.deleteReply(replyId);
        return ResponseEntity.ok(SuccessResponse.success(null));
    }
}
