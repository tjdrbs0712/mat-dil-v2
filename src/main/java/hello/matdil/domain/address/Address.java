package hello.matdil.domain.address;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Address {

    @Column(name = "address_city", nullable = false)
    private String city;

    @Column(name = "address_street", nullable = false)
    private String street;

    @Column(name = "address_detail_address", nullable = false)
    private String detailAddress;

    @Column(name = "address_latitude", nullable = false)
    private double latitude;

    @Column(name = "address_longitude", nullable = false)
    private double longitude;

    @Builder
    public Address(String city, String street, String detailAddress, double latitude, double longitude) {
        this.city = city;
        this.street = street;
        this.detailAddress = detailAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Address create(String city, String street, String detailAddress, double latitude, double longitude){
        return Address.builder()
                .city(city)
                .street(street)
                .detailAddress(detailAddress)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", city, street, detailAddress);
    }
}