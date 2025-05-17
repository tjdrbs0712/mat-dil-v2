package hello.matdil.domain.store.entity;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.menu.entity.Menu;
import hello.matdil.domain.store.exception.StoreErrorCode;
import hello.matdil.domain.store.exception.StoreException;
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
public class Store {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Address address;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long ownerId;

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

    public void addMenu(Menu menu) {
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

    public void validateBusinessHours(LocalTime openTime, LocalTime closeTime) {
        if (openTime.isAfter(closeTime)) {
            throw new StoreException(StoreErrorCode.INVALID_STORE_TIME);
        }
    }

    public void changeStoreStatus(StoreStatus newStatus) {
        if (this.status == newStatus) {
            throw new StoreException(StoreErrorCode.STORE_STATUS_UNCHANGED);
        }
        this.status = newStatus;
    }
}