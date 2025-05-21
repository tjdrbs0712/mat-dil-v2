package hello.matdil.test;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.store.dto.StoreSearchRequestDto;
import hello.matdil.domain.store.dto.StoreUpdateRequestDto;
import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.entity.StoreSortType;
import hello.matdil.domain.store.entity.StoreStatus;
import hello.matdil.domain.store.menu.dto.MenuCreateRequestDto;
import hello.matdil.domain.store.menu.dto.MenuUpdateRequestDto;
import hello.matdil.domain.store.menu.entity.Menu;
import hello.matdil.domain.store.menu.entity.MenuCategory;
import hello.matdil.domain.store.menu.entity.MenuStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;

public class TestData {
    public static Store setUpStore() {
        Store store = Store.builder()
                .name("김밥천국")
                .phoneNumber("010-1234-5678")
                .ownerId(1L)
                .address(new Address("서울시", "강남구", "101호"))
                .openTime(LocalTime.of(9, 0))
                .closeTime(LocalTime.of(22, 0))
                .minOrderPrice(10000)
                .deliveryTimeEstimate(30)
                .status(StoreStatus.OPEN)
                .build();

        ReflectionTestUtils.setField(store, "id", 1L);
        return store;
    }

    public static StoreSearchRequestDto setStoreSearchRequestDto() {
        StoreSearchRequestDto dto = new StoreSearchRequestDto();
        ReflectionTestUtils.setField(dto, "sort", StoreSortType.RATING);
        ReflectionTestUtils.setField(dto, "size", 2);
        return dto;
    }

    public static StoreUpdateRequestDto setStoreUpdateDto() {
        StoreUpdateRequestDto dto = new StoreUpdateRequestDto();
        ReflectionTestUtils.setField(dto, "name", "수정된 가게");
        ReflectionTestUtils.setField(dto, "phoneNumber", "010-9876-5432");
        ReflectionTestUtils.setField(dto, "city", "서울시");
        ReflectionTestUtils.setField(dto, "street", "서초구");
        ReflectionTestUtils.setField(dto, "detailAddress", "202호");
        ReflectionTestUtils.setField(dto, "openTime", LocalTime.of(10, 0));
        ReflectionTestUtils.setField(dto, "closeTime", LocalTime.of(23, 0));
        ReflectionTestUtils.setField(dto, "minOrderPrice", 12000);
        ReflectionTestUtils.setField(dto, "deliveryTimeEstimate", 25);
        return dto;
    }

    public static Menu setUpMenu(){
        return Menu.builder()
                .name("비빔면")
                .price(6500)
                .description("맛있어요")
                .imageUrl("url")
                .category(MenuCategory.MAIN)
                .menuStatus(MenuStatus.AVAILABLE)
                .build();
    }

    public static MenuCreateRequestDto setMenuCreateDto() {
        MenuCreateRequestDto dto = new MenuCreateRequestDto();
        ReflectionTestUtils.setField(dto, "name", "비빔면");
        ReflectionTestUtils.setField(dto, "price", 6500);
        ReflectionTestUtils.setField(dto, "description", "맛있어요");
        ReflectionTestUtils.setField(dto, "imageUrl", "url");
        ReflectionTestUtils.setField(dto, "menuStatus", MenuStatus.AVAILABLE);
        ReflectionTestUtils.setField(dto, "category", MenuCategory.MAIN);
        return dto;
    }

    public static MenuUpdateRequestDto setMenuUpdateDto() {
        MenuUpdateRequestDto dto = new MenuUpdateRequestDto();
        ReflectionTestUtils.setField(dto, "name", "수정된 매운 떡볶이");
        ReflectionTestUtils.setField(dto, "price", 8900);
        ReflectionTestUtils.setField(dto, "description", "매운맛을 강화한 떡볶이입니다.");
        ReflectionTestUtils.setField(dto, "imageUrl", "https://cdn.matdil.com/menu/spicy-tteokbokki.jpg");
        ReflectionTestUtils.setField(dto, "orderIndex", 2);
        ReflectionTestUtils.setField(dto, "category", MenuCategory.SIDE);
        ReflectionTestUtils.setField(dto, "menuStatus", MenuStatus.AVAILABLE);
        return dto;
    }

}
