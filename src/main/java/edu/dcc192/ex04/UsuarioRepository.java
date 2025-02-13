package edu.dcc192.ex04;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<UsuarioController, Long> {
    
}