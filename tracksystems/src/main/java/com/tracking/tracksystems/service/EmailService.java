package com.tracking.tracksystems.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String to, String token) {
        try {
            String verificationUrl = "http://localhost:4200/verify?token=" + token;

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setFrom("tanawatnai2014@gmail.com");
            helper.setSubject("Verify your email");

            String htmlContent = """
            <div style="font-family: Arial, sans-serif; background-color:#f4f6f9; padding:40px;">
                <div style="max-width:500px; margin:auto; background:white; padding:30px; border-radius:12px; box-shadow:0 5px 15px rgba(0,0,0,0.1);">        
                    <h2 style="color:#333;">Email Verification</h2>
                    
                    <p style="color:#555; font-size:14px;">
                        Thank you for registering. Please click the button below to verify your account.
                    </p>

                    <div style="text-align:center; margin:30px 0;">
                        <a href="%s"
                           style="background-color:#4f46e5; color:white; padding:12px 24px;
                                  text-decoration:none; border-radius:8px; font-weight:bold;
                                  display:inline-block;">
                            Verify Account
                        </a>
                    </div>

                    <p style="color:#888; font-size:12px;">
                        This link will expire soon. If you did not register, please ignore this email.
                    </p>

                </div>
            </div>
            """.formatted(verificationUrl);

            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send verification email", e);
        }
    }
}