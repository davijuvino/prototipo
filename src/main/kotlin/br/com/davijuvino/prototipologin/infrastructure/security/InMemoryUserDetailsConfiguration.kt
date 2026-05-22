package br.com.davijuvino.prototipologin.infrastructure.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager

@Configuration
class InMemoryUserDetailsConfiguration {
  @Bean
  fun userDetailsService(passwordEncoder: PasswordEncoder): UserDetailsService {
    val usuario =
      User
        .builder()
        .username("usuario")
        .password(passwordEncoder.encode("senha123"))
        .roles("USER")
        .build()
    return InMemoryUserDetailsManager(usuario)
  }
}
