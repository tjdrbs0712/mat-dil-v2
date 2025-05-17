package hello.matdil.domain.store.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import hello.matdil.domain.store.entity.QStore;
import hello.matdil.domain.store.entity.StoreStatus;
import hello.matdil.domain.user.entity.UserRole;
import org.springframework.util.StringUtils;

public class StorePredicateBuilder {

    public static BooleanBuilder build(Long userId, UserRole role, String address, String name, QStore store) {
        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression visibility = buildVisibilityFilter(userId, role, store);
        BooleanExpression addressFilter = buildAddressFilter(address, store);
        BooleanExpression nameFilter = buildNameFilter(name, store);

        if (visibility != null) builder.and(visibility);
        if (addressFilter != null) builder.and(addressFilter);
        if (nameFilter != null) builder.and(nameFilter);

        return builder;
    }

    public static BooleanExpression buildVisibilityFilter(Long userId, UserRole role, QStore store) {
        return switch (role) {
            case USER -> store.status.ne(StoreStatus.INACTIVE);
            case OWNER -> store.status.ne(StoreStatus.INACTIVE)
                    .or(store.ownerId.eq(userId));
            case ADMIN -> null;
        };
    }

    public static BooleanExpression buildAddressFilter(String address, QStore store) {
        if (!StringUtils.hasText(address)) return null;

        return store.address.city.containsIgnoreCase(address)
                .or(store.address.street.containsIgnoreCase(address));
    }

    public static BooleanExpression buildNameFilter(String name, QStore store) {
        if (!StringUtils.hasText(name)) return null;

        return store.name.containsIgnoreCase(name);
    }
}
