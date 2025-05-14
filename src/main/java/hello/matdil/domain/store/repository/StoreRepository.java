package hello.matdil.domain.store.repository;

import hello.matdil.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Long, Store> {
}
