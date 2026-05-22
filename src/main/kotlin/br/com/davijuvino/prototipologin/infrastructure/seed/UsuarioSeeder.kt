package br.com.davijuvino.prototipologin.infrastructure.seed

import br.com.davijuvino.prototipologin.domain.model.UsuarioRole
import br.com.davijuvino.prototipologin.domain.persistence.UsuarioRepository
import br.com.davijuvino.prototipologin.domain.service.UsuarioLifecycle
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class UsuarioSeeder(
  private val usuarioRepository: UsuarioRepository,
  private val usuarioLifecycle: UsuarioLifecycle,
) : ApplicationRunner {
  override fun run(args: ApplicationArguments) {
    if (usuarioRepository.count() == 0L) {
      usuarioLifecycle.criarUsuario(
        email = "admin@prototipo.local",
        rawPassword = "senha123",
        role = UsuarioRole.ADMIN,
      )
      logger.info { "Seed inicial: usuario admin@prototipo.local criado" }
    }
  }
}
