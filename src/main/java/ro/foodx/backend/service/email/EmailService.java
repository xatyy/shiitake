package ro.foodx.backend.service.email;

import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Getter
@Setter
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendHtmlMessage(String to, String subject, String message) {
        this.mailSender.send(new MimeMessagePreparator() {

            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                helper.setTo(to);
                helper.setFrom("system@foodx.ro", "FoodX");
                helper.setSubject(subject);

                Map<String, String> replacements = new HashMap<>();
                replacements.put("subject", subject);
                replacements.put("message", message);

                String htmlContent;
                try {
                    htmlContent = TemplateUtil.readTemplate("templates/email.html", replacements);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to read email template", e);
                }

                helper.setText(htmlContent, true); // Set to true to send HTML content
            }
        });
    }
}
