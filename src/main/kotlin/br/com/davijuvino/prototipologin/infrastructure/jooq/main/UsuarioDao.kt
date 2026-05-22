package br.com.davijuvino.prototipologin.infrastructure.jooq.main

import br.com.davijuvino.prototipologin.domain.model.Usuario
import br.com.davijuvino.prototipologin.domain.model.UsuarioRole
import br.com.davijuvino.prototipologin.domain.persistence.UsuarioRepository
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.lower
import org.jooq.impl.DSL.table
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.time.LocalDateTime

@Component
class UsuarioDao(
  private val dsl: DSLContext,
) : UsuarioRepository {
  private val tbl = table("usuario")
  private val id = field("id", String::class.java)
  private val email = field("email", String::class.java)
  private val senha = field("senha", String::class.java)
  private val role = field("role", String::class.java)
  private val createdDate = field("created_date", Timestamp::class.java)
  private val lastModifiedDate = field("last_modified_date", Timestamp::class.java)

  override fun count(): Long = dsl.fetchCount(tbl).toLong()

  override fun findByIdOrNull(id: String): Usuario? =
    dsl
      .selectFrom(tbl)
      .where(this.id.eq(id))
      .fetchOne()
      ?.toDomain()

  override fun findByEmailIgnoreCaseOrNull(email: String): Usuario? =
    dsl
      .selectFrom(tbl)
      .where(lower(this.email).eq(email.lowercase()))
      .fetchOne()
      ?.toDomain()

  override fun findAll(): Collection<Usuario> =
    dsl
      .selectFrom(tbl)
      .fetch()
      .map { it.toDomain() }

  override fun existsByEmailIgnoreCase(email: String): Boolean =
    dsl.fetchExists(
      dsl
        .selectFrom(tbl)
        .where(lower(this.email).eq(email.lowercase())),
    )

  override fun insert(usuario: Usuario) {
    dsl
      .insertInto(tbl)
      .set(id, usuario.id)
      .set(email, usuario.email)
      .set(senha, usuario.password)
      .set(role, usuario.role.name)
      .set(createdDate, Timestamp.valueOf(usuario.createdDate))
      .set(lastModifiedDate, Timestamp.valueOf(usuario.lastModifiedDate))
      .execute()
  }

  override fun update(usuario: Usuario) {
    dsl
      .update(tbl)
      .set(email, usuario.email)
      .set(senha, usuario.password)
      .set(role, usuario.role.name)
      .set(lastModifiedDate, Timestamp.valueOf(LocalDateTime.now()))
      .where(id.eq(usuario.id))
      .execute()
  }

  override fun delete(usuarioId: String) {
    dsl
      .deleteFrom(tbl)
      .where(id.eq(usuarioId))
      .execute()
  }

  private fun Record.toDomain(): Usuario =
    Usuario(
      email = get(email)!!,
      password = get(senha)!!,
      role = UsuarioRole.valueOf(get(role)!!),
      id = get(id)!!,
      createdDate = get(createdDate)!!.toLocalDateTime(),
      lastModifiedDate = get(lastModifiedDate)!!.toLocalDateTime(),
    )
}
