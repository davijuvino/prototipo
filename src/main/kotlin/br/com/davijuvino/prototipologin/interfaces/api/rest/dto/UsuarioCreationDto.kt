package br.com.davijuvino.prototipologin.interfaces.api.rest.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UsuarioCreationDto(
  @field:Email
  @field:NotBlank
  val email: String,
  @field:NotBlank
  @field:Size(min = 8, max = 256)
  val password: String,
)
