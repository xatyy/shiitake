package ro.foodx.backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.foodx.backend.service.email.EmailService;

@RestController
public class TestEmailController {
    @Autowired
    private EmailService emailService;

    @GetMapping("/send-test-email")
    public String sendTestEmail() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("chary.esber10@gmail.com");
            message.setFrom("system@foodx.ro");
            message.setSubject("Test Email");
            message.setText("This is a test email from Spring Boot.");
            String to = "chary.esber10@gmail.com";
            String subject = "Test Email";
            String messages = "This is a test email from Spring Boot.";
            emailService.sendHtmlMessage(to, subject, messages);
            return "Email sent successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send email.";
        }
    }
}