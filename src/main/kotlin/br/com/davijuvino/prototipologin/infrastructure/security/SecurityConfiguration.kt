package br.com.davijuvino.prototipologin.infrastructure.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfiguration {
  @Bean
  fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager = config.authenticationManager

  @Bean
  fun filterChain(http: HttpSecurity): SecurityFilterChain {
    http
      .csrf { it.disable() }
      .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
      .httpBasic { it.disable() }
      .formLogin { it.disable() }
      .logout { it.disable() }
      .authorizeHttpRequests { auth ->
        auth
          .requestMatchers("/api/v1/login", "/error")
          .permitAll()
          .anyRequest()
          .authenticated()
      }
    return http.build()
  }
}
