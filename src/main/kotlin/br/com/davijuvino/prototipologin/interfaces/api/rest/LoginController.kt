package br.com.davijuvino.prototipologin.interfaces.api.rest

import br.com.davijuvino.prototipologin.interfaces.api.rest.dto.ErrorResponseDto
import br.com.davijuvino.prototipologin.interfaces.api.rest.dto.LoginRequestDto
import br.com.davijuvino.prototipologin.interfaces.api.rest.dto.LoginResponseDto
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
class LoginController(
  private val authenticationManager: AuthenticationManager,
) {
  @PostMapping("api/v1/login")
  fun login(
    @Valid @RequestBody body: LoginRequestDto,
  ): ResponseEntity<Any> =
    try {
      val auth =
        authenticationManager.authenticate(
          UsernamePasswordAuthenticationToken(body.username, body.password),
        )
      val user = auth.principal as UserDetails
      ResponseEntity.ok(
        LoginResponseDto(
          authenticated = true,
          username = user.username,
          authorities = user.authorities.map { it.authority },
        ),
      )
    } catch (_: BadCredentialsException) {
      ResponseEntity.status(401).body(ErrorResponseDto("Credenciais invalidas"))
    } catch (_: AuthenticationException) {
      ResponseEntity.status(401).body(ErrorResponseDto("Credenciais invalidas"))
    }
}
