package br.com.davijuvino.prototipologin.infrastructure.security

import br.com.davijuvino.prototipologin.domain.persistence.UsuarioRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class PrototipoUserDetailsService(
  private val usuarioRepository: UsuarioRepository,
) : UserDetailsService {
  override fun loadUserByUsername(username: String): UserDetails =
    usuarioRepository.findByEmailIgnoreCaseOrNull(username)?.let {
      PrototipoPrincipal(it)
    } ?: throw UsernameNotFoundException(username)
}
