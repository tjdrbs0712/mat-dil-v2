package hello.matdil.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@AllArgsConstructor
public class OrderItem {

    @Column(nullable = false)
    private Long menuId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int price;
}