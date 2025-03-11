package edu.dcc192.ex04;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findById(int id);
    Usuario findByLoginAndSenha(String login, String senha);
}