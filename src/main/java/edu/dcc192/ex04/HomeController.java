package edu.dcc192.ex04;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
    @Autowired
    UsuarioRepository ur;
    
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
    public ModelAndView login(@RequestParam(required = false) String name, 
                              @RequestParam(required = false) String password, 
                              @RequestParam(required = false) String codigo, 
                              HttpSession session, 
                              RedirectAttributes redirectAttributes) {
        
        Boolean isLoggedIn = (Boolean) session.getAttribute("login");
    
        // Se o usuário já estiver logado, redireciona para o menu sem validar novamente
        if (isLoggedIn != null && isLoggedIn) {
            ModelAndView mv = new ModelAndView("menu");
            mv.addObject("userName", session.getAttribute("userName"));
            mv.addObject("dados", dados.pegaDados());
            return mv;
        }
    
        String codigoGerado = (String) session.getAttribute("codigoGerado");
    
        if (validaLogin(name, password) && verificaCodigo(codigo, codigoGerado)) {
            session.setAttribute("login", true);
            session.setAttribute("userName", name);
            session.setAttribute("password", password);
    
            ModelAndView mv = new ModelAndView("menu");
            mv.addObject("userName", name);
            mv.addObject("dados", dados.pegaDados());
            return mv;
        }
    
        redirectAttributes.addFlashAttribute("message", "Login e/ou código incorretos, tente novamente!");
        return new ModelAndView("redirect:/");
    }    

    public boolean verificaCodigo(String codigo, String codigoGerado){
        if (codigoGerado != null && codigoGerado.equals(codigo)) {
            return true;
        }
        return false;
    }

    @GetMapping("/perfil")
    public ModelAndView perfil(HttpSession session, RedirectAttributes redirectAttributes) {
        Boolean isLoggedIn = (Boolean) session.getAttribute("login");
    
        if (isLoggedIn == null || !isLoggedIn) {
            redirectAttributes.addFlashAttribute("message", "Você precisa estar logado para acessar essa página!");
            return new ModelAndView("redirect:/");
        }
    
        String userName = (String) session.getAttribute("userName");
        ModelAndView mv = new ModelAndView("perfil");
        mv.addObject("userName", userName);
        return mv;
    }
    
    @RequestMapping({"/usuarios"})
    public ModelAndView usuarios(HttpSession session, RedirectAttributes redirectAttributes) {
        Boolean isLoggedIn = (Boolean) session.getAttribute("login");
        
        if (isLoggedIn == null || !isLoggedIn) {
            redirectAttributes.addFlashAttribute("message", "Você precisa estar logado para acessar essa página!");
            return new ModelAndView("redirect:/");
        }
    
        ModelAndView mv = new ModelAndView("usuarios");
        List<UsuarioController> usuarios = ur.findAll();
        mv.addObject("usuarios", usuarios);
        return mv;
    }

    @RequestMapping({"/cadastro"})
    public ModelAndView cadastro() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("form");
        return mv;
    }

    public boolean validaLogin(String login, String password){
        List<UsuarioController> lu = ur.findAll();
        boolean existe = false;
        for(UsuarioController i: lu){
            if(i.getLogin().equals(login) && i.getSenha().equals(password)){
                existe=true;
                break;
            }
        }
       return existe;
    }

    @RequestMapping({"/create"})
    public String create(@RequestParam String name, @RequestParam String password, @RequestParam String email, RedirectAttributes redirectAttributes) {
        ur.save(new UsuarioController(name, password, email));
        redirectAttributes.addFlashAttribute("message", "Usuário criado com sucesso!");
        return "redirect:/usuarios";
    }    
    
}