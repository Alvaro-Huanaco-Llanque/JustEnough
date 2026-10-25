package com.justenough.config;

import com.justenough.model.Comercio;
import com.justenough.model.Usuario;
import com.justenough.model.enums.Rol;
import com.justenough.model.enums.Rubro;
import com.justenough.repository.ComercioRepository;
import com.justenough.repository.UsuarioRepository;
import com.justenough.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

// Al arrancar el backend se asegura de que exista la cuenta compartida del equipo de desarrollo.
// Se desactiva con app.seed.dev-account=false (por ejemplo, en producción).
@Component
@ConditionalOnProperty(name = "app.seed.dev-account", havingValue = "true", matchIfMissing = true)
public class DevDataSeeder implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ComercioRepository comercioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Value("${app.seed.dev-email}")
    private String devEmail;

    @Value("${app.seed.dev-password}")
    private String devPassword;

    @Override
    @Transactional
    public void run(String... args) {
        String email = UsuarioService.normalizarEmail(devEmail);
        Usuario usuario = usuarioRepository.findByEmail(email).orElseGet(Usuario::new);
        boolean esNuevo = usuario.getId() == null;

        if (esNuevo) {
            usuario.setNombre("Equipo JustEnough");
            usuario.setEmail(email);
            usuario.setRol(Rol.DUENIO);
        }
        // Si alguien cambió la contraseña o la desactivó, la volvemos a dejar como la conoce el equipo
        if (usuario.getPasswordHash() == null || !usuarioService.coincidePassword(devPassword, usuario.getPasswordHash())) {
            usuario.setPasswordHash(usuarioService.encriptar(devPassword));
        }
        usuario.setActivo(true);
        usuario.setEmailVerificado(true);   // la cuenta del equipo no necesita verificar el mail
        if (usuario.getComercio() == null) {
            usuario.setComercio(comercioRepository.save(Comercio.builder()
                    .nombre("Panadería de Prueba")
                    .rubro(Rubro.PANADERIA)
                    .direccion("Lima 717")
                    .localidad("CABA")
                    .provincia("Buenos Aires")
                    .telefono("-")
                    .build()));
        }
        usuarioRepository.save(usuario);

        // Cuentas creadas antes de que el registro guardara el comercio: les creamos uno para que puedan cargar datos
        for (Usuario sinComercio : usuarioRepository.findByComercioIsNull()) {
            sinComercio.setComercio(comercioRepository.save(Comercio.builder()
                    .nombre("Comercio de " + sinComercio.getNombre())
                    .rubro(Rubro.PANADERIA)
                    .localidad("Sin especificar")
                    .provincia("Sin especificar")
                    .build()));
            usuarioRepository.save(sinComercio);
        }

        System.out.println("[DevDataSeeder] Cuenta de desarrollo " + (esNuevo ? "creada" : "verificada") + ": " + email);
    }
}
