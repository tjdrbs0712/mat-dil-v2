package hello.matdil.domain.store.menu.entity;

import hello.matdil.domain.common.BaseTimeEntity;
import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.menu.dto.MenuUpdateRequestDto;
import hello.matdil.domain.store.menu.exception.MenuErrorCode;
import hello.matdil.domain.store.menu.exception.MenuException;
import hello.matdil.domain.user.entity.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "menus", indexes = {
        @Index(name = "idx_menu_store_status_order", columnList = "store_id, menu_status, order_index, id DESC")
})
public class Menu extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(length = 500)
    private String description;

    @Column(length = 500)
    private String imageUrl;

    @Column(nullable = false)
    private int orderIndex;

    @Column(length = 100)
    @Enumerated(EnumType.STRING)
    private MenuCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MenuStatus menuStatus;

    @Builder
    public Menu(String name, int price, String description,
                String imageUrl, MenuCategory category, MenuStatus menuStatus) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.orderIndex = 1;
        this.category = category;
        this.menuStatus = menuStatus;
    }

    public void assignStore(Store store) {
        this.store = store;
    }

    public void validateAccessibleTo(UserRole role, Long userId, Store store) {
        store.validateVisibleTo(role, userId);
        if (!menuStatus.isVisibleTo(role, userId, store.getOwnerId())) {
            throw new MenuException(MenuErrorCode.NO_PERMISSION);
        }
        ;
    }

    public void update(MenuUpdateRequestDto dto) {
        this.name = dto.getName();
        this.price = dto.getPrice();
        this.description = dto.getDescription();
        this.imageUrl = dto.getImageUrl();
        this.orderIndex = dto.getOrderIndex();
        this.category = dto.toMenuCategoryEnum();
        this.menuStatus = dto.toMenuStatusEnum();
    }

    public void delete() {
        if (this.menuStatus == MenuStatus.DELETE) {
            throw new MenuException(MenuErrorCode.ALREADY_DELETED);
        }
        this.menuStatus = MenuStatus.DELETE;
    }

    public boolean isAvailable() {
        return this.menuStatus == MenuStatus.AVAILABLE;
    }
}

