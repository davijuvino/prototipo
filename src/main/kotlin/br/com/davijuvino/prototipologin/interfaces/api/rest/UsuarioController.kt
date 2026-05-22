package br.com.davijuvino.prototipologin.interfaces.api.rest

import br.com.davijuvino.prototipologin.domain.model.UsuarioEmailJaCadastradoException
import br.com.davijuvino.prototipologin.domain.service.UsuarioLifecycle
import br.com.davijuvino.prototipologin.infrastructure.security.PrototipoPrincipal
import br.com.davijuvino.prototipologin.interfaces.api.rest.dto.UsuarioCreationDto
import br.com.davijuvino.prototipologin.interfaces.api.rest.dto.UsuarioDto
import br.com.davijuvino.prototipologin.interfaces.api.rest.dto.toDto
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping(value = ["api/v1/users"], produces = [MediaType.APPLICATION_JSON_VALUE])
class UsuarioController(
  private val usuarioLifecycle: UsuarioLifecycle,
) {
  @GetMapping("me")
  fun me(
    @AuthenticationPrincipal principal: PrototipoPrincipal,
  ): UsuarioDto = principal.usuario.toDto()

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  fun criar(
    @Valid @RequestBody body: UsuarioCreationDto,
  ): UsuarioDto =
    usuarioLifecycle
      .criarUsuario(email = body.email, rawPassword = body.password)
      .toDto()

  @ExceptionHandler(UsuarioEmailJaCadastradoException::class)
  fun handleEmailDuplicado(ex: UsuarioEmailJaCadastradoException): Nothing = throw ResponseStatusException(HttpStatus.CONFLICT, ex.message)
}
