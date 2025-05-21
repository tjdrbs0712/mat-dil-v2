package hello.matdil.domain.address;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public Address(String city, String street, String detailAddress) {
        this.city = city;
        this.street = street;
        this.detailAddress = detailAddress;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", city, street, detailAddress);
    }
}