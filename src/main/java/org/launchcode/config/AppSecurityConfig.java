package org.launchcode.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

/** using Java configuration **/

@Configuration
@ComponentScan(basePackages = {"org.launchcode.models"})
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    public AppSecurityConfig() {}

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/css/**", "/cheese", "/category", "/user/add", "/user/login/username", "/user/login/email").permitAll()
                    //.antMatchers("/admin/**").hasRole("ADMIN")
                    //.antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/user/login/username")
                        .defaultSuccessUrl("/cheese")
                        //.failureUrl("/user/login-error")
                        .permitAll()
                    .and()
                .logout()
                    .logoutUrl("/user/logout")
                    .logoutSuccessUrl("/user/login/username")
                    .deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .and()
                .sessionManagement()
                    .invalidSessionUrl("/user/invalidSession")
                    .sessionFixation().migrateSession()
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                    .maximumSessions(2)
                    .expiredUrl("/user/expiredSession")
        ;

                    // .addLogoutHandler(logoutHandler)
                    // .deleteCookies(cookieNamesToClear)
    }
    // "/css/**" and "/index" -- requests matched against these are fully accessible
    // requests matched against /admin*
    // requests matched against /user/** require user to be authenticated, must be assoc w/USER role


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /*auth
                .inMemoryAuthentication()
                    .passwordEncoder(passwordEncoder())
                    .withUser("user")
                        .password((passwordEncoder().encode("user")))
                        .roles("USER")
                        .and()
                    .withUser("admin")
                        .password(passwordEncoder().encode("admin"))
                        .roles("ADMIN");*/
        auth.authenticationProvider(authProvider());
    }

    // Beans

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public AuthenticationProvider authProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }

    /** first step in enabling concurrent session-control support :
     *  Bean is essential to make sure Spring Security Session registry
     *  is notified when the session is destroyed **/
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    /*@Bean
    @Override
    protected UserDetailsService userDetailsService() {

        List<UserDetails> users = new ArrayList<>();
        users.add(User.withUsername("user").password("1234").roles("USER").build());

        return new InMemoryUserDetailsManager(users);
    }*/


}
