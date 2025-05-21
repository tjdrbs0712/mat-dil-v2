package hello.matdil.domain.store.policy;


import hello.matdil.domain.store.exception.StoreErrorCode;
import hello.matdil.domain.store.exception.StoreException;
import hello.matdil.domain.user.entity.UserRole;
import org.springframework.stereotype.Component;

@Component
public class StoreCreatePolicy {

    public void validateCreatableBy(UserRole role) {
        if (!(role == UserRole.ADMIN || role == UserRole.OWNER)) {
            throw new StoreException(StoreErrorCode.NO_PERMISSION);
        }
    }
}
