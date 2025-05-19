package hello.matdil.domain.store.menu.service;

import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.exception.StoreErrorCode;
import hello.matdil.domain.store.exception.StoreException;
import hello.matdil.domain.store.menu.MenuFactory;
import hello.matdil.domain.store.menu.dto.MenuCreateRequestDto;
import hello.matdil.domain.store.menu.dto.MenuResponseDto;
import hello.matdil.domain.store.menu.entity.Menu;
import hello.matdil.domain.store.menu.repository.MenuRepository;
import hello.matdil.domain.store.repository.StoreRepository;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.test.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceImplTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private MenuFactory menuFactory;

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuServiceImpl menuService;

    private Store store;
    private Menu menu;
    private MenuCreateRequestDto requestDto;

    @BeforeEach
    void setup(){
        store = TestData.setUpStore();
        menu = TestData.setUpMenu();
        requestDto = TestData.setMenuCreateDto();
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
}