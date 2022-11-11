package com.cybersoft.food_project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecSecurityConfig {
    /*
        Dùng để khởi tạo danh sách user cứng và danh sách user này sẽ được lưu
        trữ ở RAM
     */
//    @Bean
//    public InMemoryUserDetailsManager userDetailsService(){
//        UserDetails user1 = User.withUsername("cybersoft")
//                .password(passwordEncoder().encode("123"))
//                .roles("USERS").build();
//
//        UserDetails user2 = User.withUsername("admin")
//                .password(passwordEncoder().encode("admin123"))
//                .roles("ADMIN").build();
//
//        return new InMemoryUserDetailsManager(user1,user2);
//    }

    @Autowired
    CustomAuthentProvider customAuthentProvider;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(customAuthentProvider);

        return authenticationManagerBuilder.build();
    }

    //Kiểu mã hoá dữ liệu
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /*
        Quy định các rule liên quan tới bảo mật và quyền truy cập...
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
       /*
       * antMatchers: Định nghĩa link cần xác thực
       * authenticated: Bắt buộc phải chứng thực ( đăng nhập ) vào link chỉ định ở antMatchers
       * permitAll: Cho phép truy cập full quyền vào link chỉ định ở antMatchers
       * anyRequest: Toàn bộ request gọi vào API
       *  */

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/signin").permitAll()
                .antMatchers("/signin/test").authenticated()
                .anyRequest().authenticated();

        return http.build();
    }

}
