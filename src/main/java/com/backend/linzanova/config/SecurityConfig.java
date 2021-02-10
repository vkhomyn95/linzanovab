package com.backend.linzanova.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class
SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtFIlter jwtFIlter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().disable().authorizeRequests()
                .antMatchers("/api/users/**").permitAll()
                .antMatchers("/api/users").hasRole("ADMIN")
                .antMatchers("/api/warehouses").permitAll()
                .antMatchers("/api/warehouses").permitAll()
                .antMatchers("/api/login").anonymous()
                .antMatchers(HttpMethod.GET,"/api/drops").permitAll()
                .antMatchers(HttpMethod.POST,"/api/lens").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/api/lenses/{\\d+}/photo").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/api/drops").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/api/solution").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/api/special").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/api/lenses/{\\d+}").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/api/drops/{\\d+}").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/api/drops/{\\d+}/photo").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/api/solution/{\\d+}").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/api/solution/{\\d+}/photo").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/api/special/{\\d+}").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/api/special/{\\d+}/photo").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/api/order").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/api/order/{\\d+}").authenticated()
                .antMatchers(HttpMethod.POST,"/api/order/{\\d+}").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/api/users").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/api/users/name").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/api/users/{\\d+}").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/api/users/{\\d+}").authenticated()
                .antMatchers("/api/users/bool").hasRole("ADMIN")
                .and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and().addFilterBefore(jwtFIlter, UsernamePasswordAuthenticationFilter.class);
    }
}
