package br.com.davijuvino.prototipologin.interfaces.api.rest.dto

data class LoginResponseDto(
  val authenticated: Boolean,
  val username: String,
  val authorities: List<String>,
)
