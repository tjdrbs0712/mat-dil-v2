package hello.matdil.domain.userorderstatus.repository;

import hello.matdil.domain.userorderstatus.entity.UserOrderStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserOrderStatsRepository extends JpaRepository<UserOrderStats, Long> {

    Optional<UserOrderStats> findByUserIdAndStoreId(Long userId, Long StoreId);

}
