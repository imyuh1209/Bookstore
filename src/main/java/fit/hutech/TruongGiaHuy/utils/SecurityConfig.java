package fit.hutech.TruongGiaHuy.utils;

import fit.hutech.TruongGiaHuy.services.OAuthService;
import fit.hutech.TruongGiaHuy.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;
    private final OAuthService oAuthService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    public UserDetailsService userDetailsService() {
        return userService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/webjars/**", "/", "/register", "/error", "/uploads/**").permitAll()
                .requestMatchers("/books/edit/**", "/books/add", "/books/delete/**").hasAnyAuthority("ADMIN", "BookManage")
                
                // Admin API - Common
                .requestMatchers("/api/v1/admin/me").hasAnyAuthority("ADMIN", "UserManage", "BookManage")
                
                // Admin API - Books & Banners
                .requestMatchers("/api/v1/admin/books/**", "/api/v1/admin/banners/**", "/api/v1/admin/categories/**").hasAnyAuthority("ADMIN", "BookManage")
                
                // Admin API - Users
                .requestMatchers("/api/v1/admin/users/**").hasAnyAuthority("ADMIN", "UserManage")

                // Admin API - Orders
                .requestMatchers("/api/v1/admin/orders/**").hasAnyAuthority("ADMIN")

                // Admin API - Roles, Permissions
                .requestMatchers("/api/v1/admin/roles/**", "/api/v1/admin/permissions/**").hasAnyAuthority("ADMIN")
                
                // Admin Pages - Books & Banners
                .requestMatchers("/admin/books", "/admin/books.html", "/admin/book-form.html", "/admin/banners", "/admin/banners.html").hasAnyAuthority("ADMIN", "BookManage")
                
                // Admin Pages - Users
                .requestMatchers("/admin/users", "/admin/users.html").hasAnyAuthority("ADMIN", "UserManage")

                // Admin Pages - Roles, Permissions
                .requestMatchers("/admin/roles", "/admin/roles.html", "/admin/permissions", "/admin/permissions.html").hasAnyAuthority("ADMIN")
                
                // Admin Layout/General
                .requestMatchers("/admin/**").hasAnyAuthority("ADMIN", "UserManage", "BookManage")
                
                .requestMatchers("/books", "/books/**").permitAll()
                .requestMatchers("/cart", "/cart/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/books/**", "/api/v1/categories/**").permitAll()
                .requestMatchers("/api/**").hasAnyAuthority("ADMIN", "USER")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(oAuthService)
                )
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler)
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}


