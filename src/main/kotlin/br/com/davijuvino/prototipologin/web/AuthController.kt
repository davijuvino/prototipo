package br.com.davijuvino.prototipologin.web

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class LoginRequest(
  @field:NotBlank val username: String,
  @field:NotBlank val password: String,
)

data class LoginResponse(
  val authenticated: Boolean,
  val username: String,
  val authorities: List<String>,
)

data class ErrorResponse(val error: String)

@RestController
@RequestMapping("/api/auth")
class AuthController(
  private val authenticationManager: AuthenticationManager,
) {
  @PostMapping("/login")
  fun login(
    @Valid @RequestBody body: LoginRequest,
  ): ResponseEntity<Any> =
    try {
      val auth = authenticationManager.authenticate(
        UsernamePasswordAuthenticationToken(body.username, body.password),
      )
      val user = auth.principal as UserDetails
      ResponseEntity.ok(
        LoginResponse(
          authenticated = true,
          username = user.username,
          authorities = user.authorities.map { it.authority },
        ),
      )
    } catch (_: BadCredentialsException) {
      ResponseEntity.status(401).body(ErrorResponse("Credenciais invalidas"))
    }
}
