package hello.matdil.domain.review.repository;

import hello.matdil.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewQueryRepository {
    Optional<Review> findByIdAndUserIdAndIsDeletedFalse(Long reviewId, Long userId);

}
