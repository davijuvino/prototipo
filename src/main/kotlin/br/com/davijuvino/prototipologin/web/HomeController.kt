package br.com.davijuvino.prototipologin.web

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController {
  @GetMapping("/")
  fun home(
    @AuthenticationPrincipal user: UserDetails,
    model: Model,
  ): String {
    model.addAttribute("username", user.username)
    return "home"
  }

  @GetMapping("/login")
  fun login(): String = "login"
}
