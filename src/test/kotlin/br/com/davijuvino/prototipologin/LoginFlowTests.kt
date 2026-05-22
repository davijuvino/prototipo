package br.com.davijuvino.prototipologin

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpSession
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.Base64
import kotlin.test.Test

@SpringBootTest
@AutoConfigureMockMvc
class LoginFlowTests {
  @Autowired
  lateinit var mockMvc: MockMvc

  @Autowired
  lateinit var objectMapper: ObjectMapper

  private fun basicHeader(
    user: String,
    pass: String,
  ): String = "Basic " + Base64.getEncoder().encodeToString("$user:$pass".toByteArray())

  @Test
  fun `POST api v1 login com Basic valido retorna 200 com usuario e cria sessao`() {
    val mvcResult =
      mockMvc
        .perform(
          post("/api/v1/login").header("Authorization", basicHeader("admin@prototipo.local", "senha123")),
        ).andExpect(status().isOk)
        .andExpect(jsonPath("$.email").value("admin@prototipo.local"))
        .andExpect(jsonPath("$.role").value("ADMIN"))
        .andReturn()

    val session = mvcResult.request.getSession(false)
    requireNotNull(session) { "Esperava sessao criada apos login" }
  }

  @Test
  fun `POST api v1 login sem Authorization retorna 401`() {
    mockMvc
      .perform(post("/api/v1/login"))
      .andExpect(status().isUnauthorized)
  }

  @Test
  fun `POST api v1 login com senha errada retorna 401`() {
    mockMvc
      .perform(
        post("/api/v1/login").header("Authorization", basicHeader("admin@prototipo.local", "errada")),
      ).andExpect(status().isUnauthorized)
  }

  @Test
  fun `POST api v1 login com usuario inexistente retorna 401`() {
    mockMvc
      .perform(
        post("/api/v1/login").header("Authorization", basicHeader("fulano@x.com", "qualquer")),
      ).andExpect(status().isUnauthorized)
  }

  @Test
  fun `GET api v1 users me sem autenticacao retorna 401`() {
    mockMvc
      .perform(get("/api/v1/users/me"))
      .andExpect(status().isUnauthorized)
  }

  @Test
  fun `fluxo completo login depois users me com sessao funciona`() {
    val loginResult =
      mockMvc
        .perform(
          post("/api/v1/login").header("Authorization", basicHeader("admin@prototipo.local", "senha123")),
        ).andExpect(status().isOk)
        .andReturn()

    val session = loginResult.request.getSession(false) as MockHttpSession

    mockMvc
      .perform(get("/api/v1/users/me").session(session))
      .andExpect(status().isOk)
      .andExpect(jsonPath("$.email").value("admin@prototipo.local"))
  }

  @Test
  fun `POST api v1 users cria novo usuario quando autenticado`() {
    val loginResult =
      mockMvc
        .perform(
          post("/api/v1/login").header("Authorization", basicHeader("admin@prototipo.local", "senha123")),
        ).andReturn()
    val session = loginResult.request.getSession(false) as MockHttpSession

    val body = mapOf("email" to "novo@prototipo.local", "password" to "senha-nova-123")
    mockMvc
      .perform(
        post("/api/v1/users")
          .session(session)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(body)),
      ).andExpect(status().isCreated)
      .andExpect(jsonPath("$.email").value("novo@prototipo.local"))
      .andExpect(jsonPath("$.role").value("USER"))
  }
}
