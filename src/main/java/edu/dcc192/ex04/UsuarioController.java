package edu.dcc192.ex04;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class UsuarioController {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String login;
    private String senha;
    private String email;

    public UsuarioController(){
        this(null,null,null);
    }
    
    public UsuarioController(String login, String senha, String email) {
        this.login = login;
        this.senha = senha;
        this.email = email;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    @Override
    public String toString() {
        return "Usuario [id=" + id + ", login=" + login + ", senha=" + senha + " email=" + email + "]";
    }
}
