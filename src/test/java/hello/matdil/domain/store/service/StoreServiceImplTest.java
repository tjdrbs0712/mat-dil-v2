package hello.matdil.domain.store.service;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.store.dto.*;
import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.exception.StoreErrorCode;
import hello.matdil.domain.store.exception.StoreException;
import hello.matdil.domain.store.mapper.StoreMapper;
import hello.matdil.domain.store.repository.StoreRepository;
import hello.matdil.domain.store.validator.StoreValidator;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.global.response.Cursor;
import hello.matdil.global.response.SliceResponse;
import hello.matdil.test.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StoreServiceImplTest {

    @InjectMocks
    private StoreServiceImpl storeService;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private StoreMapper storeMapper;

    @Mock
    private StoreValidator storeValidator;

    private Store store;

    @Test
    void 가게_등록_사장_또는_관리자가_아닌_경우() {
        // given
        Long userId = 2L;
        UserRole role = UserRole.USER;
        StoreCreateRequestDto requestDto = mock(StoreCreateRequestDto.class);

        // when & then
        assertThatThrownBy(() -> storeService.createStore(userId, role, requestDto))
                .isInstanceOf(StoreException.class)
                .hasMessageContaining(StoreErrorCode.NO_PERMISSION.getErrorMessage());
    }

    @Test
    void 커서_기반_정렬_성공() {
        // given
        StoreSearchRequestDto request = new StoreSearchRequestDto();
        request.setSort("rating");
        request.setSize(2);

        Long userId = 1L;
        UserRole userRole = UserRole.USER;

        Store store1 = createStore(101L, 4.5);
        Store store2 = createStore(102L, 4.2);

        List<Store> stores = List.of(store1, store2);
        Slice<Store> slice = new SliceImpl<>(stores, PageRequest.of(0, 2), true);

        given(storeRepository.findStoresByCondition(any(), any(), any())).willReturn(slice);

        // when
        SliceResponse<StoreSummaryResponseDto, Cursor> response = storeService.getStores(userId, userRole, request);

        // then
        assertThat(response.getContent()).hasSize(2);
        assertThat(response.isHasNext()).isTrue();
        assertThat(response.getNextCursor()).isNotNull();
        assertThat(response.getNextCursor().getValue()).isEqualTo(4.2);
        assertThat(response.getNextCursor().getStoreId()).isEqualTo(102L);
    }

    private Store createStore(Long id, double rating) {
        Store store = mock(Store.class);
        given(store.getId()).willReturn(id);
        given(store.getRating()).willReturn(rating);
        given(store.getAddress()).willReturn(mock(Address.class));
        return store;
    }

    @Test
    void 가게_수정_성공() {
        store = TestData.setUpStore();
        StoreUpdateRequestDto dto = TestData.setUpdateDto();

        given(storeValidator.validateStoreOwner(1L, 1L, UserRole.OWNER)).willReturn(store);

        StoreResponseDto expected = StoreResponseDto.from(store);

        StoreResponseDto result = storeService.updateStore(1L, UserRole.OWNER, 1L, dto);

        assertThat(result.getName()).isEqualTo(expected.getName());
        verify(storeValidator).validateStoreOwner(1L, 1L, UserRole.OWNER);
        verify(storeMapper).update(store, dto);
    }

    @Test
    void 권한_없는_가게_수정() {
        store = TestData.setUpStore();
        given(storeValidator.validateStoreOwner(999L, 1L, UserRole.OWNER))
                .willThrow(new StoreException(StoreErrorCode.NO_PERMISSION));

        StoreUpdateRequestDto dto = TestData.setUpdateDto();

        assertThatThrownBy(() ->
                storeService.updateStore(999L, UserRole.OWNER, 1L, dto))
                .isInstanceOf(StoreException.class)
                .hasMessageContaining(StoreErrorCode.NO_PERMISSION.getErrorMessage());

    }

    @Test
    void 존재하지_않는_가게_수정() {
        given(storeValidator.validateStoreOwner(1L, 1L, UserRole.OWNER))
                .willThrow(new StoreException(StoreErrorCode.STORE_NOT_FOUND));

        StoreUpdateRequestDto dto = TestData.setUpdateDto();

        assertThatThrownBy(() ->
                storeService.updateStore(1L, UserRole.OWNER, 1L, dto))
                .isInstanceOf(StoreException.class)
                .hasMessageContaining(StoreErrorCode.STORE_NOT_FOUND.getErrorMessage());
    }
}
