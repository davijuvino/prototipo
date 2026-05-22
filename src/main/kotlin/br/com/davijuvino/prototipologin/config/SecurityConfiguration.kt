package br.com.davijuvino.prototipologin.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
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
  fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager =
    config.authenticationManager

  @Bean
  fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
    http
      .csrf { it.disable() }
      .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
      .httpBasic { it.disable() }
      .formLogin { it.disable() }
      .logout { it.disable() }
      .authorizeHttpRequests { auth ->
        auth
          .requestMatchers("/api/auth/login", "/error").permitAll()
          .anyRequest().authenticated()
      }
    return http.build()
  }
}
