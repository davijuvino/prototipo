package br.com.davijuvino.prototipologin

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.Test

@SpringBootTest
@AutoConfigureMockMvc
class LoginFlowTests {
  @Autowired
  lateinit var mockMvc: MockMvc

  @Autowired
  lateinit var objectMapper: ObjectMapper

  private fun json(body: Map<String, String>) = objectMapper.writeValueAsString(body)

  @Test
  fun `POST api auth login com credenciais validas retorna 200 e usuario`() {
    mockMvc
      .perform(
        post("/api/auth/login")
          .contentType(MediaType.APPLICATION_JSON)
          .content(json(mapOf("username" to "usuario", "password" to "senha123"))),
      )
      .andExpect(status().isOk)
      .andExpect(jsonPath("$.authenticated").value(true))
      .andExpect(jsonPath("$.username").value("usuario"))
      .andExpect(jsonPath("$.authorities[0]").value("ROLE_USER"))
  }

  @Test
  fun `POST api auth login com senha errada retorna 401`() {
    mockMvc
      .perform(
        post("/api/auth/login")
          .contentType(MediaType.APPLICATION_JSON)
          .content(json(mapOf("username" to "usuario", "password" to "errada"))),
      )
      .andExpect(status().isUnauthorized)
      .andExpect(jsonPath("$.error").exists())
  }

  @Test
  fun `POST api auth login com usuario inexistente retorna 401`() {
    mockMvc
      .perform(
        post("/api/auth/login")
          .contentType(MediaType.APPLICATION_JSON)
          .content(json(mapOf("username" to "fulano", "password" to "qualquer"))),
      )
      .andExpect(status().isUnauthorized)
  }

  @Test
  fun `POST api auth login com body invalido retorna 400`() {
    mockMvc
      .perform(
        post("/api/auth/login")
          .contentType(MediaType.APPLICATION_JSON)
          .content(json(mapOf("username" to "", "password" to ""))),
      )
      .andExpect(status().isBadRequest)
  }
}
