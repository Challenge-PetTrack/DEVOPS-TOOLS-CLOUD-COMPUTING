package com.fiap.pettrack.config;

import com.fiap.pettrack.model.Clinica;
import com.fiap.pettrack.model.Tutor;
import com.fiap.pettrack.model.Usuario;
import com.fiap.pettrack.model.enums.PerfilEnum;
import com.fiap.pettrack.repository.IClinicaRepository;
import com.fiap.pettrack.repository.ITutorRepository;
import com.fiap.pettrack.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private ITutorRepository tutorRepository;

    @Autowired
    private IClinicaRepository clinicaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Inicializa ou atualiza as senhas com o hash BCrypt real da aplicação
        criaOuAtualizaUsuario("admin@pettrack.com", "admin123", "Administrador PetTrack", PerfilEnum.ROLE_ADMIN, null, null);

        Clinica clinica = clinicaRepository.findAll().stream().findFirst().orElse(null);
        criaOuAtualizaUsuario("vet@pettrack.com", "vet123", "Dra. Camila Veterinária", PerfilEnum.ROLE_VET, null, clinica);

        Tutor tutor = tutorRepository.findAll().stream().findFirst().orElse(null);
        criaOuAtualizaUsuario("ana.silva@icloud.com", "tutor123", "Ana Paula Silva (Tutor)", PerfilEnum.ROLE_TUTOR, tutor, null);
    }

    private void criaOuAtualizaUsuario(String email, String senhaPlana, String nome, PerfilEnum role, Tutor tutor, Clinica clinica) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseGet(Usuario::new);
        usuario.setEmail(email);
        usuario.setNome(nome);
        usuario.setSenha(passwordEncoder.encode(senhaPlana));
        usuario.setRole(role);
        usuario.setAtivo("S");
        if (tutor != null) {
            usuario.setTutor(tutor);
        }
        if (clinica != null) {
            usuario.setClinica(clinica);
        }
        usuarioRepository.save(usuario);
    }
}
