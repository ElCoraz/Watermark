package com.elcorazon.adminlte.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
/**********************************************************************************************************************/
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /******************************************************************************************************************/
    @Autowired
    private DataSource datasource;
    /******************************************************************************************************************/
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
            .csrf().and()
            .authorizeRequests()
                .antMatchers("/bootstrap/**", "/dist/**", "/plugins/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
            .formLogin()
                .failureUrl("/login?error")
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .permitAll()
                .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")
                .permitAll();
    }
    /******************************************************************************************************************/
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/api/**",
                "/v3/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }
    /******************************************************************************************************************/
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();

        jdbcUserDetailsManager.setDataSource(datasource);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        authenticationManagerBuilder.userDetailsService(jdbcUserDetailsManager).passwordEncoder(passwordEncoder);
        authenticationManagerBuilder.jdbcAuthentication().dataSource(datasource);

        if (!jdbcUserDetailsManager.userExists("admin")) {
            List<GrantedAuthority> grantedAuthoritys = new ArrayList<GrantedAuthority>();
            grantedAuthoritys.add(new SimpleGrantedAuthority("USER"));
            jdbcUserDetailsManager.createUser(new User("admin", passwordEncoder.encode("4217777"), grantedAuthoritys));
        }
    }
}