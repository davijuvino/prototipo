package br.com.davijuvino.prototipologin.domain.model

class UsuarioEmailJaCadastradoException(
  email: String,
) : RuntimeException("Email ja cadastrado: $email")
