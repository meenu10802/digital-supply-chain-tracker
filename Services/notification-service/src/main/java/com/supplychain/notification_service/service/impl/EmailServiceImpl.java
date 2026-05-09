package com.supplychain.notification_service.service.impl;

import com.supplychain.notification_service.dto.EmailRequestDto;
import com.supplychain.notification_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(
            EmailRequestDto emailRequestDto
    ) {

        try {

            SimpleMailMessage mailMessage =
                    new SimpleMailMessage();

            mailMessage.setTo(
                    emailRequestDto.getRecipientEmail()
            );

            mailMessage.setSubject(
                    emailRequestDto.getSubject()
            );

            mailMessage.setText(
                    emailRequestDto.getMessage()
            );

            javaMailSender.send(mailMessage);

            log.info(
                    "Email sent successfully to {}",
                    emailRequestDto.getRecipientEmail()
            );

        } catch (Exception ex) {

            log.error(
                    "Email sending failed",
                    ex
            );

            throw new RuntimeException(
                    "Failed to send email"
            );
        }
    }
}