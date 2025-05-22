package hello.matdil.domain.store.entity;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.common.BaseTimeEntity;
import hello.matdil.domain.store.dto.StoreUpdateRequestDto;
import hello.matdil.domain.store.exception.StoreErrorCode;
import hello.matdil.domain.store.exception.StoreException;
import hello.matdil.domain.store.menu.entity.Menu;
import hello.matdil.domain.user.entity.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "stores",
        indexes = {
                @Index(name = "idx_store_rating_id", columnList = "rating DESC, id DESC"),
                @Index(name = "idx_store_review_id", columnList = "reviewCount DESC, id DESC"),
                @Index(name = "idx_store_delivery_id", columnList = "deliveryTimeEstimate ASC, id ASC"),
                @Index(name = "idx_store_name_id", columnList = "name ASC, id ASC")
        }
)
public class Store extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Address address;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long ownerId;

    @Column(length = 500)
    private String imageUrl;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StoreStatus status;

    @Column(nullable = false)
    private int minOrderPrice;

    @Column(nullable = false)
    private int deliveryTimeEstimate;

    @Column(nullable = false)
    private double rating;

    @Column(nullable = false)
    private int reviewCount;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus = new ArrayList<>();

    @Builder
    public Store(Address address, String name, Long ownerId, String phoneNumber,
                 LocalTime openTime, LocalTime closeTime, StoreStatus status,
                 int minOrderPrice, int deliveryTimeEstimate) {
        this.address = address;
        this.name = name;
        this.ownerId = ownerId;
        this.phoneNumber = phoneNumber;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.status = status;
        this.minOrderPrice = minOrderPrice;
        this.deliveryTimeEstimate = deliveryTimeEstimate;
        this.rating = 0.0;
        this.reviewCount = 0;
    }

    public void addMenu(Menu menu, Long userId, UserRole role) {
        validateModifiableBy(userId, role);
        menus.add(menu);
        menu.assignStore(this);
    }

    public void updateInfo(String name, String phoneNumber, Address address,
                           LocalTime openTime, LocalTime closeTime,
                           int minOrderPrice, int deliveryTimeEstimate) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.minOrderPrice = minOrderPrice;
        this.deliveryTimeEstimate = deliveryTimeEstimate;
    }

    public void validateVisibleTo(UserRole role, Long userId) {
        if(!this.status.isVisibleTo(role, userId, this.ownerId)){
            throw new StoreException(StoreErrorCode.NO_PERMISSION);
        }
    }

    public void validateBusinessHours(LocalTime openTime, LocalTime closeTime) {
        if (openTime.isAfter(closeTime)) {
            throw new StoreException(StoreErrorCode.INVALID_STORE_TIME);
        }
    }

    public void validateModifiableBy(Long userId, UserRole role) {
        boolean isAdmin = role == UserRole.ADMIN;
        boolean isOwner = role == UserRole.OWNER && this.ownerId.equals(userId);

        if (!(isAdmin || isOwner)) {
            throw new StoreException(StoreErrorCode.NO_PERMISSION);
        }
    }

    public void changeStoreStatus(StoreStatus newStatus) {
        if (this.status == newStatus) {
            throw new StoreException(StoreErrorCode.STORE_STATUS_UNCHANGED);
        }
        this.status = newStatus;
    }

    public void update(StoreUpdateRequestDto dto) {
        this.name = dto.getName();
        this.phoneNumber = getPhoneNumber();
        this.address = new Address(dto.getCity(), dto.getStreet(), dto.getDetailAddress());
        this.openTime = dto.getOpenTime();
        this.closeTime = dto.getCloseTime();
        this.minOrderPrice = dto.getMinOrderPrice();
        this.deliveryTimeEstimate = dto.getDeliveryTimeEstimate();
    }
}