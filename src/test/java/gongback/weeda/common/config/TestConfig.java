package gongback.weeda.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import gongback.weeda.common.config.converter.JwtAuthenticationConverter;
import gongback.weeda.common.config.handler.CustomAccessDeniedHandler;
import gongback.weeda.common.config.handler.CustomAuthenticationEntryPoint;
import gongback.weeda.common.config.handler.CustomServerAuthenticationFailureHandler;
import gongback.weeda.common.jwt.JwtAuthenticationManager;
import gongback.weeda.common.jwt.JwtSupport;
import gongback.weeda.common.properties.JwtProperties;
import gongback.weeda.service.CustomUserDetailsService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import(SecurityConfig.class)
@MockBean({JwtAuthenticationManager.class, JwtSupport.class, CustomUserDetailsService.class, JwtProperties.class})
@EnableConfigurationProperties(value = {JwtProperties.class})
public class TestConfig {
    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    CustomAccessDeniedHandler accessDeniedHandler(ObjectMapper objectMapper) {
        return new CustomAccessDeniedHandler(objectMapper);
    }

    @Bean
    CustomAuthenticationEntryPoint authenticationEntryPoint(ObjectMapper objectMapper) {
        return new CustomAuthenticationEntryPoint(objectMapper);
    }

    @Bean
    CustomServerAuthenticationFailureHandler customServerAuthenticationFailureHandler(ObjectMapper objectMapper) {
        return new CustomServerAuthenticationFailureHandler(objectMapper);
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(JwtProperties jwtProperties) {
        return new JwtAuthenticationConverter(jwtProperties);
    }
}
