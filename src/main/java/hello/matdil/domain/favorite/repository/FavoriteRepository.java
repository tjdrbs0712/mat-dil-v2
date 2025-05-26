package hello.matdil.domain.favorite.repository;

import hello.matdil.domain.favorite.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByUserIdAndStoreId(Long userId, Long storeId);
}
