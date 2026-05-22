package br.com.davijuvino.prototipologin.interfaces.api.rest.dto

import jakarta.validation.constraints.NotBlank

data class LoginRequestDto(
  @field:NotBlank val username: String,
  @field:NotBlank val password: String,
)
