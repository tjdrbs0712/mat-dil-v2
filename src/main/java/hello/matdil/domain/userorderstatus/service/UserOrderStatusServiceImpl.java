package hello.matdil.domain.userorderstatus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserOrderStatusServiceImpl implements UserOrderStatusService {

    private final UserOrderStatusRedisService statusRedisService;

    @Transactional
    public void increaseOrderCount(Long userId, Long storeId, LocalDateTime orderedAt) {
        statusRedisService.increaseOrderCount(userId, storeId, orderedAt);
    }
}
