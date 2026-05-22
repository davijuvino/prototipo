package br.com.davijuvino.prototipologin.interfaces.api.rest.dto

import br.com.davijuvino.prototipologin.domain.model.Usuario
import java.time.LocalDateTime

data class UsuarioDto(
  val id: String,
  val email: String,
  val role: String,
  val createdDate: LocalDateTime,
  val lastModifiedDate: LocalDateTime,
)

fun Usuario.toDto(): UsuarioDto =
  UsuarioDto(
    id = id,
    email = email,
    role = role.name,
    createdDate = createdDate,
    lastModifiedDate = lastModifiedDate,
  )
