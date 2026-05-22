package br.com.davijuvino.prototipologin.domain.model

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime
import java.util.UUID

data class Usuario(
  @field:Email(regexp = ".+@.+\\..+")
  @field:NotBlank
  val email: String,
  @field:NotBlank
  val password: String,
  val role: UsuarioRole = UsuarioRole.USER,
  val id: String = UUID.randomUUID().toString(),
  val createdDate: LocalDateTime = LocalDateTime.now(),
  val lastModifiedDate: LocalDateTime = createdDate,
)
