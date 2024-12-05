package edu.dcc192.ex04;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
    @Autowired
    private GeradorSenha senha;

    @Autowired
    private Dados dados;

    @GetMapping("/")
    public ModelAndView home(HttpSession session) {
        String codigoGerado = senha.GerarSenha();
        session.setAttribute("codigoGerado", codigoGerado);
        ModelAndView mv = new ModelAndView("login");
        mv.addObject("senha", codigoGerado);
        return mv;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("message", "Logout realizado com sucesso!");
        return "redirect:/";
    }

    @GetMapping("/menu")
    public ModelAndView login(@RequestParam String name, @RequestParam String codigo, HttpSession session, RedirectAttributes redirectAttributes) {
        String codigoGerado = (String) session.getAttribute("codigoGerado");

        if (codigoGerado != null && codigoGerado.equals(codigo)) {
            ModelAndView mv = new ModelAndView("menu");
            mv.addObject("userName", name);
            mv.addObject("dados", dados.pegaDados());
            return mv;
        }

        redirectAttributes.addFlashAttribute("message", "Código incorreto. Tente novamente.");
        return new ModelAndView("redirect:/");
    }
}