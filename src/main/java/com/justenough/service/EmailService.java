package com.justenough.service;

import com.justenough.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

// Envía los mails de la aplicación. Si no hay cuenta de mail configurada (MAIL_USERNAME vacío en el .env),
// en vez de mandar el mail imprime el link en la consola del backend, así se puede probar en desarrollo.
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String remitente;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public void enviarVerificacion(Usuario usuario) {
        String link = frontendUrl + "/verificar?token=" + URLEncoder.encode(usuario.getTokenVerificacion(), StandardCharsets.UTF_8);

        if (remitente == null || remitente.isBlank()) {
            System.out.println("\n[EmailService] No hay mail configurado (MAIL_USERNAME). Link de verificación para "
                    + usuario.getEmail() + ":\n" + link + "\n");
            return;
        }

        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom("Just Enough <" + remitente + ">");
        mensaje.setTo(usuario.getEmail());
        mensaje.setSubject("Verificá tu email - Just Enough");
        mensaje.setText("""
                ¡Hola %s!

                Gracias por crear tu cuenta en Just Enough. Para activarla, abrí este link:

                %s

                El link vence en 24 horas. Si no creaste esta cuenta, ignorá este mensaje.

                El equipo de Just Enough
                """.formatted(usuario.getNombre(), link));
        mailSender.send(mensaje);
    }
}
