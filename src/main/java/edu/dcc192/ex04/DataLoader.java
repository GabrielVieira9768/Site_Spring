package edu.dcc192.ex04;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository) {
        return args -> {
            if (usuarioRepository.findById(1) == null) {
                Usuario admin = new Usuario("Admin", "password", "admin@example.com", true);
                usuarioRepository.save(admin);
                System.out.println("Usuário Admin criado com sucesso!");
            } else {
                System.out.println("Usuário Admin já existe.");
            }
        };
    }
}
