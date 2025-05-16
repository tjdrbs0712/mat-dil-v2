package hello.matdil.domain.store.entity;

import com.querydsl.core.types.OrderSpecifier;

public enum StoreSortType {
    RATING, DELIVERY_TIME, REVIEW, NAME;

    public static StoreSortType from(String sort) {
        return switch (sort.toLowerCase()) {
            case "deliverytime" -> DELIVERY_TIME;
            case "review" -> REVIEW;
            case "name" -> NAME;
            default -> RATING;
        };
    }

    public OrderSpecifier<?> toOrderSpecifier(QStore store) {
        return switch (this) {
            case DELIVERY_TIME -> store.deliveryTimeEstimate.asc();
            case REVIEW -> store.reviewCount.desc();
            case NAME -> store.name.asc();
            case RATING -> store.rating.desc();
        };
    }
}