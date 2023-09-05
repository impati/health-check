package com.example.healthcheck.api.v1.resolver;

import static java.util.Objects.*;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.example.healthcheck.exception.ErrorCode;
import com.example.healthcheck.exception.HealthCheckException;
import com.example.healthcheck.security.BringCustomer;
import com.example.healthcheck.security.Customer;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private final BringCustomer bringCustomer;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return Customer.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(
        final MethodParameter parameter,
        final ModelAndViewContainer mavContainer,
        final NativeWebRequest webRequest,
        final WebDataBinderFactory binderFactory
    ) {
        return bringCustomer.bring(convert(requireNonNull(webRequest.getNativeRequest(HttpServletRequest.class))));
    }

    private String convert(final HttpServletRequest request) {
        final String header = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(header) && header.startsWith(BEARER)) {
            return header.replace(BEARER, "");
        }

        throw new HealthCheckException(ErrorCode.INVALID_TOKEN);
    }
}
