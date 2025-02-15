package team16.literaryassociation.config;

import org.camunda.bpm.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import team16.literaryassociation.security.TokenUtils;
import team16.literaryassociation.security.auth.JwtAuthenticationFilter;
import team16.literaryassociation.security.auth.RestAuthenticationEntryPoint;
import team16.literaryassociation.services.impl.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private CustomUserDetailsService jwtUserDetailsService;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private IdentityService identityService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Autowired
    TokenUtils tokenUtils;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http    .cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()
                .authorizeRequests()
                //.antMatchers("/api/auth/**", "/api/reader/**").permitAll()
                //.anyRequest().authenticated().and()
                .anyRequest().permitAll().and()//zbog camunde
                .addFilterBefore(new JwtAuthenticationFilter(tokenUtils, jwtUserDetailsService, identityService),
                        BasicAuthenticationFilter.class);

        http.csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) {
        // JwtAuthenticationFilter ignores all below listed paths
        web.ignoring().antMatchers(HttpMethod.POST, "/api/auth/login", "/api/index/**", "/api/reader/**");
        web.ignoring().antMatchers(HttpMethod.GET, "/", "/webjars/**", "/*.html", "/favicon.ico", "/**/*.html",
                "/**/*.css", "/**/*.js", "/**/*.pdf");
    }

}
