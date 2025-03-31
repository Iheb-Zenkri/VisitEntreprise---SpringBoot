package Spring.Visit.UserModule.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendEmail(String to, String resetLink) throws MessagingException {
        String subject = "Reset Your Password";
        String emailContent = getEmailTemplate(resetLink);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(emailContent, true);

        mailSender.send(message);
    }

    private String getEmailTemplate(String resetLink) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Réinitialisation de mot de passe</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f4f4f4;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 30px auto;\n" +
                "            background: #ffffff;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .header {\n" +
                "            font-size: 24px;\n" +
                "            font-weight: bold;\n" +
                "            color: #333;\n" +
                "        }\n" +
                "        .content {\n" +
                "            font-size: 16px;\n" +
                "            color: #555;\n" +
                "            line-height: 1.5;\n" +
                "        }\n" +
                "        .reset-button {\n" +
                "            display: inline-block;\n" +
                "            padding: 12px 24px;\n" +
                "            font-size: 16px;\n" +
                "            background-color: #007bff;\n" +
                "            color: #fff !important;\n" +  // Important to override any link styles
                "            text-decoration: none;\n" +
                "            border-radius: 5px;\n" +
                "            margin-top: 20px;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            font-size: 14px;\n" +
                "            color: #888;\n" +
                "            margin-top: 20px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">Réinitialisez votre mot de passe</div>\n" +
                "        <p class=\"content\">\n" +
                "            Vous avez demandé à réinitialiser votre mot de passe. Cliquez sur le bouton ci-dessous pour le réinitialiser.\n" +
                "        </p>\n" +
                "        <a href=\"" + resetLink + "\" class=\"reset-button\">Réinitialiser le mot de passe</a>\n" +
                "        <p class=\"content\">\n" +
                "            Si vous n'êtes pas à l'origine de cette demande, veuillez ignorer cet e-mail. Ce lien expirera dans 30 minutes.\n" +
                "        </p>\n" +
                "        <div class=\"footer\">© 2025 Visit d'Entreprise. Tous droits réservés.</div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }


}
