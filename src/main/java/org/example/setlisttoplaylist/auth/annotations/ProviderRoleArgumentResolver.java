package org.example.setlisttoplaylist.auth.annotations;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import org.example.setlisttoplaylist.auth.domain.Provider;

import java.util.Objects;
import java.util.Optional;

@Component
public class ProviderRoleArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ProviderRole.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assert auth != null;
        String requestedProvider = Optional.ofNullable(webRequest.getParameter("provider"))
                .orElse(webRequest.getParameter("mode"));

        if (requestedProvider != null) {
            Provider provider = Provider.fromKey(requestedProvider);
            String expectedRole = provider.role();
            Optional<String> matched = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority).filter(Objects::nonNull)
                    .filter(expectedRole::equals)
                    .findFirst();

            if (matched.isPresent()) {
                return matched.get();
            }

            // Fallback: if the requested provider role is missing, use any existing provider role
            // so the call can still proceed with the authenticated provider in session.
            return auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(Objects::nonNull)
                    .filter(r -> r.startsWith("ROLE_"))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No authenticated provider found"));
        }

        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).filter(Objects::nonNull)
                .filter(r -> r.startsWith("ROLE_"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No provider role found"));
    }
}
