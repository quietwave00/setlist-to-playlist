package org.example.setlisttoplaylist.auth.annotations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final ProviderRoleArgumentResolver providerRoleArgumentResolver;

    public WebConfig(ProviderRoleArgumentResolver resolver) {
        this.providerRoleArgumentResolver = resolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(providerRoleArgumentResolver);
    }
}

