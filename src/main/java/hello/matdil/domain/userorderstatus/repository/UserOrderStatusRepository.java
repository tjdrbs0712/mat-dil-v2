package hello.matdil.domain.userorderstatus.repository;

import hello.matdil.domain.userorderstatus.entity.UserOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserOrderStatusRepository extends JpaRepository<UserOrderStatus, Long> {

    Optional<UserOrderStatus> findByUserIdAndStoreId(Long userId, Long StoreId);

}
