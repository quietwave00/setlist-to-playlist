package org.example.setlisttoplaylist.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.setlisttoplaylist.auth.domain.Provider;
import org.example.setlisttoplaylist.auth.session.AuthSessionKeys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class ProviderAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            if (session != null) {
                String activeProviderKey = (String) session.getAttribute(AuthSessionKeys.ACTIVE_PROVIDER);

                if (activeProviderKey != null) {
                    Provider provider = Provider.fromKey(activeProviderKey);
                    List<SimpleGrantedAuthority> authorities =
                            List.of(new SimpleGrantedAuthority(provider.role()));

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    "sessionUser",
                                    null,
                                    authorities
                            );

                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(auth);
                    SecurityContextHolder.setContext(context);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}

