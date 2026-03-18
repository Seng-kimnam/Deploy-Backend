package _bbu.lawfirmapi.configuration;

import _bbu.lawfirmapi.jwt.JwtAuthEntryPoint;
import _bbu.lawfirmapi.jwt.JwtAuthFilter;
import _bbu.lawfirmapi.models.Entity.Role;
import _bbu.lawfirmapi.models.Enumerations.RoleStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String[] roleStatus = {RoleStatus.LAWYER.toString() , RoleStatus.ADMIN.toString() };
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(request -> request
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/api/v1/auths/login",
                                "/api/v1/auths/register",
                                "/api/v1/auths/reset-password",
                                "/api/v1/auths/**",
//                                "/api/v1/appointments/**",
                                "/api/v1/courts/**",
                                "/api/v1/cases/**",
                                "/api/v1/expertises/**",
                                "/api/v1/app-user/**",
                                "/api/v1/files/**",
                                "/api/v1/services/**",
//                                "/api/v1/clients/**",
                                "/api/v1/documents/**",
                                "/api/v1/lawyers/**",
                                // this endpoint will be protected for admin only the rest of get method for everyone
                                "/api/v1/categories/**",
                                "/api/v1/tasks/**",
                                "/api/v1/roles/**",
                                "/api/v1/verifications/**",
                                "/api/v1/clients/**"
                        ).permitAll()
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/v1/admins/**").hasRole(roleStatus[1])
  //                              .requestMatchers("/api/v1/clients/**").hasRole(roleStatus[1])
//                        .requestMatchers(HttpMethod.DELETE, "/api/v1/admin/lawyers/**")
//                        .hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/api/v1/admin/lawyers/**")
//                        .hasRole("ADMIN")

                        .requestMatchers("/api/v1/appointments/**").hasAnyRole(roleStatus[0] , roleStatus[1])
                        .requestMatchers("/api/v1/admin/**").hasRole(roleStatus[1])

                        .requestMatchers("/api/v1/lawyers/lawyer-profile/").hasAnyRole( roleStatus[0])
                                .requestMatchers("/api/v1/categories/**").hasRole(roleStatus[0])
//                                .requestMatchers(HttpMethod.GET, "/api/v1/documents/**").permitAll()
//                                .requestMatchers(HttpMethod.POST, "/api/v1/documents").hasAnyRole(roleStatus[0] , roleStatus[1])
//                                .requestMatchers(HttpMethod.PUT, "/api/v1/documents").hasAnyRole(roleStatus[0] , roleStatus[1])
//                                .requestMatchers(HttpMethod.DELETE, "/api/v1/documents/**").hasAnyRole(roleStatus[0] , roleStatus[1])
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthEntryPoint)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
