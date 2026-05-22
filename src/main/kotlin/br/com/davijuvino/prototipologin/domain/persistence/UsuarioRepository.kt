package br.com.davijuvino.prototipologin.domain.persistence

import br.com.davijuvino.prototipologin.domain.model.Usuario

interface UsuarioRepository {
  fun count(): Long

  fun findByIdOrNull(id: String): Usuario?

  fun findByEmailIgnoreCaseOrNull(email: String): Usuario?

  fun findAll(): Collection<Usuario>

  fun existsByEmailIgnoreCase(email: String): Boolean

  fun insert(usuario: Usuario)

  fun update(usuario: Usuario)

  fun delete(usuarioId: String)
}
