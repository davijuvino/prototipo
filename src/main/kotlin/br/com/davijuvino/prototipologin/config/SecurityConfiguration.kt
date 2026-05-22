package br.com.davijuvino.prototipologin.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfiguration {
  @Bean
  fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

  @Bean
  fun userDetailsService(passwordEncoder: PasswordEncoder): UserDetailsService {
    val usuario = User.builder()
      .username("usuario")
      .password(passwordEncoder.encode("senha123"))
      .roles("USER")
      .build()
    return InMemoryUserDetailsManager(usuario)
  }

  @Bean
  fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
    http
      .authorizeHttpRequests { auth ->
        auth
          .requestMatchers("/login", "/css/**", "/js/**", "/error").permitAll()
          .anyRequest().authenticated()
      }
      .formLogin { form ->
        form
          .loginPage("/login")
          .defaultSuccessUrl("/", true)
          .permitAll()
      }
      .logout { logout ->
        logout
          .logoutSuccessUrl("/login?logout")
          .permitAll()
      }
    return http.build()
  }
}
