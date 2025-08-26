package com.dolbom.hanium_project.service;


import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service

public class SmsService {

    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecret;

    @Value("${coolsms.sender.number}")
    private String senderNumber;

    private DefaultMessageService messageService;

    @PostConstruct
    private void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    public void sendSms(String to, String messageText) {
        Message message = new Message();
        message.setFrom(senderNumber);
        message.setTo(to);
        message.setText(messageText);

        try {
            this.messageService.sendOne(new SingleMessageSendingRequest(message));
            System.out.println("문자 발송 성공! 수신번호: " + to);
        } catch (Exception e) {
            System.err.println("문자 발송 실패: " + e.getMessage());
        }
    }
}