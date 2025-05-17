package hello.matdil.domain.menu.entity;

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
public class Menu {

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
    private String category;

    @Column(nullable = false)
    private boolean isAvailable;

    @Column(nullable = false)
    private boolean isSoldOut;

    @Builder
    public Menu(String name, int price, String description,
                String imageUrl, String category, boolean isAvailable, boolean isSoldOut) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
        this.isAvailable = isAvailable;
        this.isSoldOut = isSoldOut;
    }

    public void assignStore(Store store) {
        this.store = store;
    }
}
