package hello.matdil.domain.review.reviewreply.repository;

import hello.matdil.domain.review.reviewreply.entity.ReviewReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewReplyRepository extends JpaRepository<ReviewReply, Long> {
}
