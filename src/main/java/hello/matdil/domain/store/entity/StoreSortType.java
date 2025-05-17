package hello.matdil.domain.store.entity;

public enum StoreSortType {
    RATING, DELIVERY_TIME, REVIEW, NAME, ID;

    public static StoreSortType from(String sort) {
        return switch (sort.toLowerCase()) {
            case "deliverytime" -> DELIVERY_TIME;
            case "review" -> REVIEW;
            case "name" -> NAME;
            case "id" -> ID;
            default -> RATING;
        };
    }
}