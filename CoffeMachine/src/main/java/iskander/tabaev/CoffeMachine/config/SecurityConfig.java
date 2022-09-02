package iskander.tabaev.CoffeMachine.config;

import iskander.tabaev.CoffeMachine.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    protected void configure(HttpSecurity httpSecurity) throws Exception {
//        В методе происходит конфигурирование Spring Security
//        В методе можно конфигурировать авторизацию
//
//        Даем доступ всем пользователям к инструменту SwaggerUI
        //Даем доступ к заказу кофе только пользователям с ролями Юзер и
        //Администратор. Запросы к странице пользователей может отправлять только администратор
        httpSecurity.authorizeRequests()
                .antMatchers("/api/persons").hasRole("ADMIN")
                .antMatchers("/api/coffeeOrders").hasAnyRole("ADMIN", "USER")
                .antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().
                defaultSuccessUrl("/Hello").permitAll();

        //Отключение защиты CSRF
        httpSecurity.cors().and().csrf().disable();

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    //Настраиваем аутентификацию, указываем какой провайдер использовать для Аутентификации
    //Но в данном случае провайдер
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //В данном случае провайдер не обязателен, так как логин и пароль хранится у меня на сервере
        //Поэтому можно воспользоваться методом userDetailsService()
//        auth.authenticationProvider()

        auth.userDetailsService(personDetailsService)
                .passwordEncoder(getPasswordEncoder());
    }

    //Бин, который сообщает спрингу, как у нас шифруется пароль
    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
