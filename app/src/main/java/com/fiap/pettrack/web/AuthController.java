package com.fiap.pettrack.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Authentication authentication,
            Model model) {

        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/dashboard";
        }

        if (error != null) {
            model.addAttribute("mensagemErro", "E-mail ou senha inválidos. Verifique suas credenciais.");
        }
        if (logout != null) {
            model.addAttribute("mensagemSucesso", "Sessão encerrada com sucesso!");
        }

        return "auth/login";
    }

    @GetMapping("/403")
    public String acessoNegado(Model model, Authentication auth) {
        if (auth != null) {
            model.addAttribute("usuario", auth.getName());
            model.addAttribute("perfis", auth.getAuthorities());
        }
        return "error/403";
    }
}
