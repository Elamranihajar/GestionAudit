package gestionAudits.controller;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {

    public static void sendEmail(String recipient, String subject, String body) {
        // Configuration des propriétés pour le serveur SMTP
        String host = "smtp.gmail.com";
        final String senderEmail = "mouhsinenaghach@gmail.com"; // Remplacez par votre email
        final String senderPassword = "opaj mkns kxlw nddx"; // Remplacez par votre mot de passe

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        // Créer une session avec authentification
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Construire le message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(body);

            // Envoyer le message
            Transport.send(message);
            System.out.println("E-mail envoyé avec succès à " + recipient);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'envoi de l'e-mail.");
        }
    }

}
