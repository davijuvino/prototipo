package br.com.davijuvino.prototipologin.interfaces.api.rest

import br.com.davijuvino.prototipologin.infrastructure.security.PrototipoPrincipal
import br.com.davijuvino.prototipologin.interfaces.api.rest.dto.UsuarioDto
import br.com.davijuvino.prototipologin.interfaces.api.rest.dto.toDto
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
class LoginController {
  @PostMapping("api/v1/login")
  fun login(
    @AuthenticationPrincipal principal: PrototipoPrincipal,
  ): UsuarioDto = principal.usuario.toDto()
}
