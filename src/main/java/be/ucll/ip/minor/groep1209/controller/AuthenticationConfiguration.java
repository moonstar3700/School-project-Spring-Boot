package be.ucll.ip.minor.groep1209.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class AuthenticationConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth
                .inMemoryAuthentication()
                .withUser("user")
                .password(encoder.encode("t"))
                .roles("USER")
                .and()
                .withUser("admin")
                .password(encoder.encode("t"))
                .roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/coins/overview/**").authenticated()
                    .antMatchers("/coins/search/**").authenticated()
                    .antMatchers("/coins/**").hasRole("ADMIN")
                    .antMatchers("/club/overview/**").authenticated()
                    .antMatchers("/club/search/**").authenticated()
                    .antMatchers("/club/**").hasRole("ADMIN")
                    .antMatchers("/").permitAll()
                    .antMatchers("/api/**").permitAll()
                    .antMatchers("/home").permitAll()
                    .antMatchers("/h2/**").permitAll()
                    .antMatchers("/css/Stylesheet.css").permitAll()
                    .antMatchers("/favicon.ico").permitAll()
                    .antMatchers("/clubhuis/overview").authenticated()
                    .antMatchers("/clubhuis/**").hasRole("ADMIN")
                    .and()
                .formLogin()
                    .loginPage("/home").permitAll()
                    .and()
                .logout()
                    .permitAll()
                    .and()
                .headers().frameOptions().disable()
                    .and()
                    .csrf().disable()
                    .httpBasic();
    }
}
