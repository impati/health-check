package com.example.healthcheck.api.v1.resolver;

import com.example.healthcheck.exception.ErrorCode;
import com.example.healthcheck.exception.HealthCheckException;
import com.example.healthcheck.security.BringCustomer;
import com.example.healthcheck.security.Customer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TokenArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private final BringCustomer bringCustomer;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Customer.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return bringCustomer.bring(convert(Objects.requireNonNull(webRequest.getNativeRequest(HttpServletRequest.class))));
    }

    private String convert(HttpServletRequest request){
        String header = request.getHeader(AUTHORIZATION);
        if(StringUtils.hasText(header) && header.startsWith(BEARER)){
            return header.replace(BEARER,"");
        }
        throw new HealthCheckException(ErrorCode.INVALID_TOKEN);
    }
}
