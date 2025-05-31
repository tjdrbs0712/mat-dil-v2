package hello.matdil.domain.store.repository;

import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.entity.StoreStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long>, StoreQueryRepository {
    boolean existsByIdAndStatusNotIn(Long storeId, List<StoreStatus> excludedStatuses);
}
