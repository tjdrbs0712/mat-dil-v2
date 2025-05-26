package hello.matdil.domain.store.query;

import com.querydsl.core.BooleanBuilder;
import hello.matdil.domain.store.entity.QStore;
import hello.matdil.domain.user.entity.UserRole;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StorePredicateBuilderTest {

    QStore store = QStore.store;

    @Test
    void userCannotSeeInactiveStore() {
        BooleanBuilder builder = StorePredicateBuilder.build(
                null, UserRole.USER, null, null, store
        );

        assertThat(builder.toString()).contains("store.status not in [INACTIVE, DELETED]");
    }

    @Test
    void ownerCanSeeOwnInactiveStore() {
        Long userId = 1L;
        BooleanBuilder builder = StorePredicateBuilder.build(
                userId, UserRole.OWNER, null, null, store
        );

        assertThat(builder.toString()).contains("store.ownerId = 1");
    }

    @Test
    void searchByAddressAndName() {
        BooleanBuilder builder = StorePredicateBuilder.build(
                null, UserRole.ADMIN, "서울", "치킨", store
        );

        String str = builder.toString();
        assertThat(str).contains("containsIc(store.address.city", "containsIc(store.name");
    }
}