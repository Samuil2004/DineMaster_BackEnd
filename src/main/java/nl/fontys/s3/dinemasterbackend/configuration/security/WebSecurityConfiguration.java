package nl.fontys.s3.dinemasterbackend.configuration.security;


import nl.fontys.s3.dinemasterbackend.configuration.security.auth.AuthenticationRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
@Configuration
public class WebSecurityConfiguration {
    private static final String[] SWAGGER_UI_RESOURCES = {
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/swagger-ui/**"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity,
                                           AuthenticationEntryPoint authenticationEntryPoint,
                                           AuthenticationRequestFilter authenticationRequestFilter) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(configurer ->
                        configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(registry ->
//                        registry.anyRequest().permitAll()
//                )
                .authorizeHttpRequests(registry -> registry
                        //When making a cross-origin request (from one domain to another),
                        // the browser first sends an OPTIONS request to the server to
                        // determine whether the actual request is safe to send.
                        // This preflight request is made automatically by the browser
                        // before sending the actual request -> The OPTIONS request checks:
                        //Which HTTP methods the server supports.
                        //Which headers are allowed in the actual request.
                        //Whether the actual request can include credentials (e.g., cookies or authorization headers).
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/customers/**", "/auth/logIn/**", "/auth/tokens/refresh/**", "/notifications/**", "/users/password/reset/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/items/categories/**", "/items/{id}").permitAll()
                        .requestMatchers(SWAGGER_UI_RESOURCES).permitAll()
                        .requestMatchers(HttpMethod.GET, "/ws/**").permitAll()
                        //.requestMatchers("/students/**").authenticated()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(configure -> configure.authenticationEntryPoint(authenticationEntryPoint))
                .addFilterBefore(authenticationRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
