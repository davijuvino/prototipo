package br.com.davijuvino.prototipologin.infrastructure.security

import br.com.davijuvino.prototipologin.domain.model.Usuario
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class PrototipoPrincipal(
  val usuario: Usuario,
) : UserDetails {
  override fun getAuthorities(): Collection<GrantedAuthority> = listOf(SimpleGrantedAuthority(usuario.role.toAuthority()))

  override fun getPassword(): String = usuario.password

  override fun getUsername(): String = usuario.email

  override fun isAccountNonExpired(): Boolean = true

  override fun isAccountNonLocked(): Boolean = true

  override fun isCredentialsNonExpired(): Boolean = true

  override fun isEnabled(): Boolean = true
}
