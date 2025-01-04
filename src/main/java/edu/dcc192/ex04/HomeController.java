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
        session.setAttribute("login", false);
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
    public ModelAndView login( @RequestParam(required = false) String name, @RequestParam(required = false) String codigo, HttpSession session, RedirectAttributes redirectAttributes) {
        String codigoGerado = (String) session.getAttribute("codigoGerado");
        ModelAndView mv = new ModelAndView("menu");
    
        if (verificaCodigo(codigo, codigoGerado) && (boolean) session.getAttribute("login") == false) {
            session.setAttribute("login", true);
            session.setAttribute("userName", name);
            mv.addObject("userName", name);
            mv.addObject("dados", dados.pegaDados());
            return mv;
        } else if((boolean) session.getAttribute("login") == true) {
            mv.addObject("userName", (String) session.getAttribute("userName"));
            mv.addObject("dados", dados.pegaDados());
            return mv;
        }
    
        redirectAttributes.addFlashAttribute("message", "Código incorreto, tente novamente!");
        return new ModelAndView("redirect:/");
    }

    public boolean verificaCodigo(String codigo, String codigoGerado){
        if (codigoGerado != null && codigoGerado.equals(codigo)) {
            return true;
        }
        return false;
    }

    @GetMapping("/Perfil")
    public ModelAndView perfil(HttpSession session) {
        String userName = (String) session.getAttribute("userName");
        ModelAndView mv = new ModelAndView("perfil");
        mv.addObject("userName", userName);
        return mv;
    }
    
}