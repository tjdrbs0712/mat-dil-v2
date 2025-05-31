package hello.matdil.domain.store.validator;

import hello.matdil.domain.store.exception.StoreErrorCode;
import hello.matdil.domain.store.exception.StoreException;
import hello.matdil.domain.user.entity.UserRole;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.BiConsumer;

@Component
public class StoreExistenceValidatorStrategy {

    private final Map<UserRole, BiConsumer<Long, Long>> validatorMap;

    public StoreExistenceValidatorStrategy(StoreValidator validator) {
        this.validatorMap = Map.of(
                UserRole.USER,   (userId, storeId) -> validator.validateUserExists(storeId),
                UserRole.OWNER,  (userId, storeId) -> validator.validateOwnerAccessible(storeId, userId),
                UserRole.ADMIN,  (userId, storeId) -> validator.validateAdminExists(storeId)
        );
    }

    public void validate(UserRole role, Long userId, Long storeId) {
        BiConsumer<Long, Long> validator = validatorMap.get(role);
        if (validator == null) {
            throw new StoreException(StoreErrorCode.NO_PERMISSION);
        }
        validator.accept(userId, storeId);
    }
}
