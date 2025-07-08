package com.bioesencia.backend.controller;

import com.bioesencia.backend.model.Usuario;
import com.bioesencia.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final Map<String, UsuarioTemporal> usuariosPendientes = new HashMap<>();

    @PostMapping("/pre-registro")
    public ResponseEntity<String> preRegistro(@RequestBody Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().isBlank() || usuario.getNombre().length() > 50) {
            return ResponseEntity.badRequest().body("Nombre requerido y máximo 50 caracteres");
        }

        if (usuario.getApellido() == null || usuario.getApellido().isBlank() || usuario.getApellido().length() > 50) {
            return ResponseEntity.badRequest().body("Apellido requerido y máximo 50 caracteres");
        }

        String email = usuario.getEmail();
        if (email == null || email.isBlank() || email.length() > 100 ||
                !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            return ResponseEntity.badRequest().body("Correo inválido o demasiado largo (máx 100 caracteres)");
        }

        // Verificar si el correo ya existe
        if (usuarioService.buscarPorEmail(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya existe una cuenta registrada con este correo.");
        }

        String password = usuario.getPassword();
        if (password == null || password.isBlank() || password.length() > 60 ||
                !password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.#_-])[A-Za-z\\d@$!%*?&.#_-]{8,}$")) {
            return ResponseEntity.badRequest().body(
                    "Contraseña débil. Debe tener al menos 8 caracteres, incluir mayúsculas, minúsculas, números y un carácter especial.");
        }

        // Aqui se genera el codigo y se guarda en memoria
        String codigo = String.format("%06d", new Random().nextInt(1000000));
        usuariosPendientes.put(email, new UsuarioTemporal(usuario, codigo));

        try {
            enviarCorreo(email, codigo);
            return ResponseEntity.ok("Código enviado al correo");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error enviando el correo");
        }
    }

    @PostMapping("/verificar-registro")
    public ResponseEntity<?> verificarRegistro(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String codigoIngresado = request.get("codigo");

        UsuarioTemporal temporal = usuariosPendientes.get(email);

        if (temporal != null && temporal.getCodigo().equals(codigoIngresado)) {
            Usuario creado = usuarioService.registrar(temporal.getUsuario());
            usuariosPendientes.remove(email);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Código incorrecto");
    }

    private void enviarCorreo(String email, String codigo) throws MessagingException {
        final String username = "biosencia04@gmail.com";
        final String password = "zcsq iwpe ummc ruji";

        // Configuración de las propiedades del correo
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject("Código de verificación");
        message.setText("Tu código de verificación es: " + codigo);
        Transport.send(message);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario loginRequest) {
        Optional<Usuario> optionalUsuario = usuarioService.buscarPorEmail(loginRequest.getEmail());

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            boolean passwordValida = usuarioService.checkPassword(loginRequest.getPassword(), usuario.getPassword());

            if (passwordValida) {
                return ResponseEntity.ok(usuario);
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Clase auxiliar para manejar usuarios temporales en el registro
    static class UsuarioTemporal {
        private final Usuario usuario;
        private final String codigo;

        public UsuarioTemporal(Usuario usuario, String codigo) {
            this.usuario = usuario;
            this.codigo = codigo;
        }

        public Usuario getUsuario() {
            return usuario;
        }

        public String getCodigo() {
            return codigo;
        }
    }
}
