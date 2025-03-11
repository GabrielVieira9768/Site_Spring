package edu.dcc192.ex04;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String login;
    private String senha;
    private boolean cargo;

    @Email(message = "O email deve ser válido")
    private String email;

    public Usuario(){
        this(null, null, null, false);
    }
    
    public Usuario(String login, String senha, String email, Boolean cargo) {
        this.login = login;
        this.senha = senha;
        this.email = email;
        this.cargo = (cargo != null) ? cargo : false;
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

    public boolean ehAdmin(){
        return cargo;
    }

    @Override
    public String toString() {
        return "Usuario [id=" + id + ", login=" + login + ", senha=" + senha + " email=" + email + "]";
    }
}
