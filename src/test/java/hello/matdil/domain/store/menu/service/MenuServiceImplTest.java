package hello.matdil.domain.store.menu.service;

import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.exception.StoreErrorCode;
import hello.matdil.domain.store.exception.StoreException;
import hello.matdil.domain.store.menu.dto.MenuCreateRequestDto;
import hello.matdil.domain.store.menu.dto.MenuCursorRequestDto;
import hello.matdil.domain.store.menu.dto.MenuCursorResponseDto;
import hello.matdil.domain.store.menu.dto.MenuResponseDto;
import hello.matdil.domain.store.menu.entity.Menu;
import hello.matdil.domain.store.menu.factory.MenuFactory;
import hello.matdil.domain.store.menu.repository.MenuRepository;
import hello.matdil.domain.store.repository.StoreRepository;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.global.response.SliceResponse;
import hello.matdil.global.util.pagination.PageAssembler;
import hello.matdil.test.TestData;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class MenuServiceImplTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private MenuFactory menuFactory;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private PageAssembler pageAssembler;

    @InjectMocks
    private MenuServiceImpl menuService;

    private Store store;
    private Menu menu;
    private MenuCreateRequestDto requestDto;
    private MenuResponseDto responseDto;
    private MenuCursorRequestDto cursor;

    @BeforeEach
    void setup(){
        store = TestData.setUpStore();
        menu = TestData.setUpMenu();
        requestDto = TestData.setMenuCreateDto();
        responseDto = MenuResponseDto.from(menu);
        cursor = new MenuCursorRequestDto(3, null, null);
    }

    @Test
    void 메뉴_등록_성공(){
        given(storeRepository.findById(1L)).willReturn(Optional.ofNullable(store));
        given(menuFactory.createMenu(any())).willReturn(menu);

        MenuResponseDto result = menuService.createMenu(1L, UserRole.OWNER, 1L, requestDto);

        assertThat(result.getName()).isEqualTo(menu.getName());
    }

    @Test
    void 메뉴_등록_권한이_없는_경우(){
        given(storeRepository.findById(1L)).willReturn(Optional.ofNullable(store));

        assertThatThrownBy(() ->
                menuService.createMenu(1L, UserRole.USER, 1L, requestDto))
                .isInstanceOf(StoreException.class)
                .hasMessageContaining(StoreErrorCode.NO_PERMISSION.getErrorMessage());
    }

    @Test
    void 가게가_존재하지_않으면_예외를_던진다() {
        // given
        given(storeRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> menuService.getMenus(1L, UserRole.USER, 1L, cursor))
                .isInstanceOf(StoreException.class)
                .hasMessageContaining(StoreErrorCode.STORE_NOT_FOUND.getErrorMessage());
    }

    @Test
    void 메뉴_리스트를_정상적으로_조회하고_조립한다() {
        // given
        given(storeRepository.findById(1L)).willReturn(Optional.of(store));
        given(menuRepository.findMenusByCursor(1L, UserRole.OWNER, 1L, cursor))
                .willReturn(List.of(menu));

        SliceResponse<MenuResponseDto, MenuCursorResponseDto> fakeSlice = SliceResponse.of(
                List.of(responseDto),
                false,
                null
        );
        given(pageAssembler.assemble(
                anyList(),
                anyInt(),
                any(Function.class),
                any(Function.class)
        )).willReturn(fakeSlice);

        // when
        SliceResponse<MenuResponseDto, MenuCursorResponseDto> result =
                menuService.getMenus(1L, UserRole.OWNER, 1L, cursor);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo(menu.getName());
        assertThat(result.isHasNext()).isFalse();
    }

    @Test
    void 메뉴_단건_조회_성공(){
        // given
        store.addMenu(menu, 1L, UserRole.ADMIN);
        given(menuRepository.findByIdWithStore(1L, 1L)).willReturn(Optional.ofNullable(menu));
        // when
        MenuResponseDto result = menuService.getMenu(1L, UserRole.ADMIN, 1L, 1L);
        // then
        assertThat(result.getName()).isEqualTo(menu.getName());

    }
}