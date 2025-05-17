package hello.matdil.auth.resolver;

import hello.matdil.auth.annotation.LoginUser;
import hello.matdil.auth.model.AuthUser;
import hello.matdil.auth.security.UserDetailsImpl;
import hello.matdil.domain.user.exception.UserException;
import hello.matdil.global.exception.CommonErrorCode;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class)
                && parameter.getParameterType().equals(AuthUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            LoginUser loginUser = parameter.getParameterAnnotation(LoginUser.class);
            if (Objects.requireNonNull(loginUser).required()) {
                throw new UserException(CommonErrorCode.UNAUTHORIZED); // 커스텀 예외
            }
            return AuthUser.anonymous();
        }

        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        return AuthUser.authenticated(principal.getUserId(), principal.getRole(), principal.getUserRole());
    }
}