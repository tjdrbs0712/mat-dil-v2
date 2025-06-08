package hello.matdil.domain.store.validator;

import hello.matdil.domain.store.exception.StoreErrorCode;
import hello.matdil.domain.store.exception.StoreException;
import hello.matdil.domain.user.entity.UserRole;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Consumer;

@Component
public class StoreExistenceValidatorStrategy {

    private final Map<UserRole, Consumer<Long>> validatorMap;

    public StoreExistenceValidatorStrategy(StoreValidator storeValidator) {
        this.validatorMap = Map.of(
                UserRole.USER, storeValidator::validateUserExists,
                UserRole.OWNER, storeValidator::validateUserExists,
                UserRole.ADMIN, storeValidator::validateAdminExists
        );
    }

    public void validate(UserRole role, Long storeId) {
        Consumer<Long> validator = validatorMap.get(role);
        if (validator == null) {
            throw new StoreException(StoreErrorCode.NO_PERMISSION);
        }
        validator.accept(storeId);
    }
}

