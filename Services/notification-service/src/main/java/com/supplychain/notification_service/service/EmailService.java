package com.supplychain.notification_service.service;

import com.supplychain.notification_service.dto.EmailRequestDto;

public interface EmailService {

    void sendEmail(
            EmailRequestDto emailRequestDto
    );
}