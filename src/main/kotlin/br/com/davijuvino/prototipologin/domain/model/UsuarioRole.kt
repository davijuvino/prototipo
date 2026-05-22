package br.com.davijuvino.prototipologin.domain.model

enum class UsuarioRole {
  USER,
  ADMIN,
  ;

  fun toAuthority(): String = "ROLE_$name"
}
