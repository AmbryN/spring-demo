package edu.ambryn.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import javax.sql.DataSource;
import java.util.Arrays;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
            .configurationSource(httpServletRequest -> {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.applyPermitDefaultValues();
                corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT"));
                corsConfiguration.setAllowedHeaders(Arrays.asList("X-Requested-With",
                                                                  "Origin",
                                                                  "Content-Type",
                                                                  "Accept",
                                                                  "Authorization",
                                                                  "Access-Control-Allow-Origin"));
                return corsConfiguration;
            })
            .and()
            .csrf()
            .disable()
            .authorizeRequests()
            .antMatchers("/auth")
            .permitAll()
            .antMatchers("/register")
            .permitAll()
            .antMatchers("/admin/**")
            .hasRole("ADMIN")
            .antMatchers("/**")
            .hasAnyRole("ADMIN", "USER")
            .anyRequest()
            .authenticated()
            .and()
            .exceptionHandling()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    //    @Override
    //    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //        auth.jdbcAuthentication()
    //                .dataSource(dataSource)
    //                .usersByUsernameQuery("SELECT email, password, 1 FROM user WHERE email = ?")
    //                .authoritiesByUsernameQuery(
    //                        " SELECT email, R.name " +
    //                                " FROM user" +
    //                                " LEFT JOIN user_roles AS UR ON user.id = UR.user_id" +
    //                                " LEFT JOIN role AS R ON UR.roles_id = R.id" +
    //                                " WHERE email = ?"
    //                );
    //    }

    @Bean
    public PasswordEncoder creationPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
