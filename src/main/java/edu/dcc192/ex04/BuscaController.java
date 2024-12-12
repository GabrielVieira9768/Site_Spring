package edu.dcc192.ex04;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BuscaController {
    @GetMapping("/busca")
    public ModelAndView busca(){
        ModelAndView mv = new ModelAndView("busca.html");
        return mv;
    }
}
