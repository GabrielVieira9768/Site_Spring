package edu.dcc192.ex04;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

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
        session.setAttribute("autorizado", false);
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
    public ModelAndView login(@RequestParam(required = false) String login, 
                              @RequestParam(required = false) String senha, 
                              @RequestParam(required = false) String codigo, 
                              HttpSession session, 
                              RedirectAttributes redirectAttributes) {
        
        Boolean isLoggedIn = (Boolean) session.getAttribute("autorizado");
    
        // Se o usuário já estiver logado, redireciona para o menu sem validar novamente
        if (isLoggedIn != null && isLoggedIn) {
            ModelAndView mv = new ModelAndView("menu");
            mv.addObject("login", session.getAttribute("login"));
            mv.addObject("dados", dados.pegaDados());
            return mv;
        }
    
        String codigoGerado = (String) session.getAttribute("codigoGerado");
    
        if (validaLogin(login, senha) && verificaCodigo(codigo, codigoGerado)) {
            session.setAttribute("autorizado", true);
            session.setAttribute("login", login);
            session.setAttribute("senha", senha);

            Usuario usuario = ur.findByLoginAndSenha(login, senha);
            session.setAttribute("cargo", usuario.ehAdmin());
    
            ModelAndView mv = new ModelAndView("menu");
            mv.addObject("login", login);
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
        Boolean isLoggedIn = (Boolean) session.getAttribute("autorizado");
    
        if (isLoggedIn == null || !isLoggedIn) {
            redirectAttributes.addFlashAttribute("message", "Você precisa estar logado para acessar essa página!");
            return new ModelAndView("redirect:/");
        }
    
        String login = (String) session.getAttribute("login");
        ModelAndView mv = new ModelAndView("perfil");
        mv.addObject("login", login);
        return mv;
    }
    
    @RequestMapping({"/usuarios"})
    public ModelAndView usuarios(HttpSession session, RedirectAttributes redirectAttributes) {
        Boolean autorizado = (Boolean) session.getAttribute("autorizado");
        Boolean cargo = (Boolean) session.getAttribute("cargo");
        Boolean isLoggedIn = Boolean.TRUE.equals(autorizado) && Boolean.TRUE.equals(cargo);
        
        if (isLoggedIn == null || !isLoggedIn) {
            redirectAttributes.addFlashAttribute("message", "Você precisa estar logado para acessar essa página!");
            return new ModelAndView("redirect:/");
        } else if (cargo == null || cargo == false){
            redirectAttributes.addFlashAttribute("message", "Você não possui autorização para acessar essa página!");
            return new ModelAndView("redirect:/");
        }
    
        ModelAndView mv = new ModelAndView("usuarios");
        List<Usuario> usuarios = ur.findAll();
        mv.addObject("usuarios", usuarios);
        return mv;
    }

    @RequestMapping({"/cadastro"})
    public ModelAndView cadastro(HttpSession session, RedirectAttributes redirectAttributes) {
        Boolean autorizado = (Boolean) session.getAttribute("autorizado");
        Boolean cargo = (Boolean) session.getAttribute("cargo");
        Boolean isLoggedIn = Boolean.TRUE.equals(autorizado) && Boolean.TRUE.equals(cargo);
        
        if (isLoggedIn == null || !isLoggedIn) {
            redirectAttributes.addFlashAttribute("message", "Você precisa estar logado para acessar essa página!");
            return new ModelAndView("redirect:/");
        } else if (cargo == null || cargo == false){
            redirectAttributes.addFlashAttribute("message", "Você não possui autorização para acessar essa página!");
            return new ModelAndView("redirect:/");
        }
        
        ModelAndView mv = new ModelAndView();
        mv.setViewName("form");
        return mv;
    }

    public boolean validaLogin(String login, String senha){
        List<Usuario> lu = ur.findAll();
        boolean existe = false;
        for(Usuario i: lu){
            if(i.getLogin().equals(login) && i.getSenha().equals(senha)){
                existe=true;
                break;
            }
        }
       return existe;
    }

    @RequestMapping("/create")
    public String create(@Valid @ModelAttribute Usuario usuario, 
                        BindingResult result, 
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", result.getAllErrors());
            return "redirect:/cadastro";
        }

        ur.save(usuario);
        redirectAttributes.addFlashAttribute("message", "Usuário criado com sucesso!");
        return "redirect:/usuarios";
    }
    
}