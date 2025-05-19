package hello.matdil.domain.store.menu.entity;

import hello.matdil.domain.common.BaseTimeEntity;
import hello.matdil.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "menus")
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

    @Column(length = 100)
    @Enumerated(EnumType.STRING)
    private MenuCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MenuStatus status;

    @Builder
    public Menu(String name, int price, String description,
                String imageUrl, MenuCategory category, MenuStatus menuStatus) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
        this.status = menuStatus;
    }

    public void assignStore(Store store) {
        this.store = store;
    }
}
