package br.com.davijuvino.prototipologin.domain.service

import br.com.davijuvino.prototipologin.domain.model.Usuario
import br.com.davijuvino.prototipologin.domain.model.UsuarioEmailJaCadastradoException
import br.com.davijuvino.prototipologin.domain.persistence.UsuarioRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger {}

@Service
class UsuarioLifecycle(
  private val usuarioRepository: UsuarioRepository,
  private val passwordEncoder: PasswordEncoder,
) {
  @Transactional
  fun criarUsuario(
    email: String,
    rawPassword: String,
    role: br.com.davijuvino.prototipologin.domain.model.UsuarioRole =
      br.com.davijuvino.prototipologin.domain.model.UsuarioRole.USER,
  ): Usuario {
    if (usuarioRepository.existsByEmailIgnoreCase(email)) {
      throw UsuarioEmailJaCadastradoException(email)
    }
    val usuario =
      Usuario(
        email = email.lowercase(),
        password = passwordEncoder.encode(rawPassword),
        role = role,
      )
    usuarioRepository.insert(usuario)
    logger.info { "Usuario criado: ${usuario.email}" }
    return usuario
  }

  @Transactional
  fun atualizarSenha(
    usuario: Usuario,
    novaSenha: String,
  ): Usuario {
    val atualizado = usuario.copy(password = passwordEncoder.encode(novaSenha))
    usuarioRepository.update(atualizado)
    logger.info { "Senha alterada para o usuario ${usuario.email}" }
    return atualizado
  }
}
