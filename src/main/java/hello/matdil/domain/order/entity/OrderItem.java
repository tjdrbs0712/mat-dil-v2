package hello.matdil.domain.order.entity;

import hello.matdil.domain.store.menu.entity.Menu;
import hello.matdil.domain.store.menu.exception.MenuErrorCode;
import hello.matdil.domain.store.menu.exception.MenuException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private Long menuId;

    @Column(nullable = false)
    private String menuName;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int price;

    @Builder
    public OrderItem(Long menuId, String menuName, int quantity, int price){
        this.menuId = menuId;
        this.menuName = menuName;
        this.quantity = quantity;
        this.price = price;
    }

    public static OrderItem of(Menu menu, int quantity) {
        if (!menu.isAvailable()) {
            throw new MenuException(MenuErrorCode.UNAVAILABLE_MENU);
        }

        return OrderItem.builder()
                .menuId(menu.getId())
                .menuName(menu.getName())
                .quantity(quantity)
                .price(menu.getPrice() * quantity)
                .build();
    }

    public void assignOrder(Order order) {
        this.order = order;
    }
}
