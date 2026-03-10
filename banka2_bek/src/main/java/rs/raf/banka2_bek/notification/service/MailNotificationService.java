package rs.raf.banka2_bek.notification.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import rs.raf.banka2_bek.notification.template.PasswordResetEmailTemplate;

@Service
public class MailNotificationService {

    private final JavaMailSender mailSender;
    private final String fromAddress;
    private final String passwordResetUrlBase;
    private final String passwordResetPagePath;
    private final PasswordResetEmailTemplate passwordResetEmailTemplate;

    public MailNotificationService(JavaMailSender mailSender,
                                   PasswordResetEmailTemplate passwordResetEmailTemplate,
                                   @Value("${spring.mail.username}") String fromAddress,
                                   @Value("${notification.password-reset-url-base}") String passwordResetUrlBase,
                                   @Value("${notification.password-reset-page-path:/reset-password}") String passwordResetPagePath) {
        this.mailSender = mailSender;
        this.passwordResetEmailTemplate = passwordResetEmailTemplate;
        this.fromAddress = fromAddress;
        this.passwordResetUrlBase = passwordResetUrlBase;
        this.passwordResetPagePath = passwordResetPagePath;
    }

    public void sendPasswordResetMail(String toEmail, String token) {
        String resetLink = passwordResetUrlBase + passwordResetPagePath + "?token=" + token;
        String subject = passwordResetEmailTemplate.buildSubject();
        String html = passwordResetEmailTemplate.buildBody(resetLink);

        HtmlMailSender.sendHtmlMail(mailSender, fromAddress, toEmail, subject, html);
    }
}

