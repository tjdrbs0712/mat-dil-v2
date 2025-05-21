package hello.matdil.domain.store.service;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.store.dto.*;
import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.exception.StoreErrorCode;
import hello.matdil.domain.store.exception.StoreException;
import hello.matdil.domain.store.factory.StoreFactory;
import hello.matdil.domain.store.policy.StoreCreatePolicy;
import hello.matdil.domain.store.repository.StoreRepository;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.global.response.SliceResponse;
import hello.matdil.global.util.pagination.PageAssembler;
import hello.matdil.test.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class StoreServiceImplTest {

    @InjectMocks
    private StoreServiceImpl storeService;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private StoreFactory storeFactory;

    @Mock
    private PageAssembler pageAssembler;

    @Mock
    private StoreCreatePolicy storeCreatePolicy;

    private Store store;

    @Test
    void 가게_등록_사장_또는_관리자가_아닌_경우() {
        // given
        Long userId = 2L;
        UserRole role = UserRole.USER;
        StoreCreateRequestDto requestDto = mock(StoreCreateRequestDto.class);

        doThrow(new StoreException(StoreErrorCode.NO_PERMISSION))
                .when(storeCreatePolicy).validateCreatableBy(role);

        // when & then
        assertThatThrownBy(() -> storeService.createStore(userId, role, requestDto))
                .isInstanceOf(StoreException.class)
                .hasMessageContaining(StoreErrorCode.NO_PERMISSION.getErrorMessage());
    }

    @Test
    void 커서_기반_정렬_성공() {
        // given
        StoreSearchRequestDto request = TestData.setStoreSearchRequestDto();
        store = TestData.setUpStore();
        StoreSummaryResponseDto responseDto = StoreSummaryResponseDto.from(store);


        given(storeRepository.findStoresByCondition(any(), any(), any())).willReturn(List.of(store));

        SliceResponse<StoreSummaryResponseDto, StoreCursorResponseDto> fakeSlice = SliceResponse.of(
                List.of(responseDto),
                false,
                null
        );

        given(pageAssembler.assemble(
                any(),
                anyInt(),
                any(Function.class),
                any(Function.class)))
                .willReturn(fakeSlice);

        // when
        SliceResponse<StoreSummaryResponseDto, StoreCursorResponseDto> response =
                storeService.getStores(1L, UserRole.USER, request);

        // then
        assertThat(response.getContent()).hasSize(1);
        assertThat(response.isHasNext()).isFalse();
        assertThat(response.getContent().get(0).getName()).isEqualTo(store.getName());
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
        // given
        store = TestData.setUpStore();
        StoreUpdateRequestDto dto = TestData.setStoreUpdateDto();
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));

        // when
        StoreResponseDto result = storeService.updateStore(1L, UserRole.OWNER, 1L, dto);

        // then
        assertThat(result.getName()).isEqualTo(dto.getName());
    }

    @Test
    void 권한_없는_가게_수정() {
        // given
        store = TestData.setUpStore();
        given(storeRepository.findById(1L)).willReturn(Optional.of(store));

        StoreUpdateRequestDto dto = TestData.setStoreUpdateDto();

        // when & then
        assertThatThrownBy(() ->
                storeService.updateStore(999L, UserRole.OWNER, 1L, dto))
                .isInstanceOf(StoreException.class)
                .hasMessageContaining(StoreErrorCode.NO_PERMISSION.getErrorMessage());

    }

    @Test
    void 존재하지_않는_가게_수정() {
        // given
        StoreUpdateRequestDto dto = TestData.setStoreUpdateDto();
        given(storeRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() ->
                storeService.updateStore(1L, UserRole.OWNER, 1L, dto))
                .isInstanceOf(StoreException.class)
                .hasMessageContaining(StoreErrorCode.STORE_NOT_FOUND.getErrorMessage());
    }
}
