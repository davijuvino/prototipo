package br.com.davijuvino.prototipologin

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.Test

@SpringBootTest
@AutoConfigureMockMvc
class LoginFlowTests {
  @Autowired
  lateinit var mockMvc: MockMvc

  @Test
  fun `pagina de login responde 200 para visitantes`() {
    mockMvc.perform(get("/login")).andExpect(status().isOk)
  }

  @Test
  fun `home redireciona para login quando nao autenticado`() {
    mockMvc.perform(get("/")).andExpect(status().is3xxRedirection)
  }

  @Test
  fun `login com credenciais validas autentica usuario`() {
    mockMvc.perform(formLogin("/login").user("usuario").password("senha123"))
      .andExpect(authenticated().withUsername("usuario"))
  }

  @Test
  fun `login com credenciais invalidas falha`() {
    mockMvc.perform(formLogin("/login").user("usuario").password("errada"))
      .andExpect(unauthenticated())
  }
}
