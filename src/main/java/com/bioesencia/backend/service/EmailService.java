package com.bioesencia.backend.service;

import com.bioesencia.backend.model.Cita;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.util.Properties;

@Service
public class EmailService {

    private final String username = "biosencia04@gmail.com";
    private final String password = "zcsq iwpe ummc ruji"; // App password

    private Properties getMailProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        return props;
    }

    private Session createSession() {
        return Session.getInstance(getMailProperties(), new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public void enviarCorreoCodigo(String destinatario, String codigo) {
        try {
            Message mensaje = new MimeMessage(createSession());
            mensaje.setFrom(new InternetAddress(username));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject("Código de verificación");
            mensaje.setText("Tu código de verificación es: " + codigo);

            Transport.send(mensaje);
            System.out.println("📧 Correo enviado exitosamente a: " + destinatario);
        } catch (Exception e) {
            System.out.println("❌ ERROR AL ENVIAR CORREO:");
            e.printStackTrace(); 
        }
    }

    public void enviarCorreoCita(Cita cita) {
        try {
            Message mensaje = new MimeMessage(createSession());
            mensaje.setFrom(new InternetAddress(username));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(cita.getUsuario().getEmail()));
            mensaje.setSubject("Detalles de tu cita");
            mensaje.setText("Detalles de la cita:\n" +
                    "Fecha y hora: " + cita.getFechaHora() + "\n" +
                    "Duración: " + cita.getDuracion() + " minutos\n" +
                    "Servicio: " + cita.getServicio() + "\n" +
                    "Notas: " + cita.getNotas());

            Transport.send(mensaje);
            System.out.println("📧 Correo de cita enviado exitosamente a: " + cita.getUsuario().getEmail());
        } catch (Exception e) {
            System.out.println("❌ ERROR AL ENVIAR CORREO:");
            e.printStackTrace();
        }
    }

    public void enviarCorreoConAdjunto(String destinatario, String codigoOrden, byte[] adjuntoPdf) {
        try {
            Message mensaje = new MimeMessage(createSession());
            mensaje.setFrom(new InternetAddress(username));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject("Orden de compra: " + codigoOrden);

            String cuerpoTexto = String.join("\n",
                    "¡Muchas gracias por preferirnos!",
                    "",
                    "Te adjuntamos tu orden de compra " + codigoOrden,
                    "Quedamos atentos a tu visita para retirar tu pedido."
            );

            MimeBodyPart textoPart = new MimeBodyPart();
            textoPart.setText(cuerpoTexto);

            MimeBodyPart adjuntoPart = new MimeBodyPart();
            DataSource source = new ByteArrayDataSource(adjuntoPdf, "application/pdf");
            adjuntoPart.setDataHandler(new DataHandler(source));
            adjuntoPart.setFileName("Orden-" + codigoOrden + ".pdf");

            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(textoPart);
            multiParte.addBodyPart(adjuntoPart);

            mensaje.setContent(multiParte);

            Transport.send(mensaje);
            System.out.println("📧 PDF enviado exitosamente a: " + destinatario);
        } catch (Exception e) {
            System.out.println("❌ ERROR AL ENVIAR CORREO CON PDF:");
            e.printStackTrace();
        }
    }

    public void enviarCorreoRecuperacionContrasenia(String destinatario) {
    }
}
