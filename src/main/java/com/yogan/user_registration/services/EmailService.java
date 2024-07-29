package com.yogan.user_registration.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine engine;

    public EmailService(JavaMailSender mailSender, SpringTemplateEngine engine) {
        this.mailSender = mailSender;
        this.engine = engine;
    }

    @Async
    public void send(String to, String templateName,String username, String url) throws MessagingException {
        if(!StringUtils.hasLength(templateName)){
            templateName="confirm-email";
        }

        MimeMessage mimeMessage=mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name());

        Map<String, Object> properties=new HashMap<>();
        properties.put("username", username);
        properties.put("confirmationUrl",url);

        Context context=new Context();
        context.setVariables(properties);

        helper.setFrom("yogananthaml03@gmail.com");
        helper.setTo(to);
        helper.setSubject("Account activation mail");
        String template=engine.process(templateName, context);
        helper.setText(template,true);
        mailSender.send(mimeMessage);
    }
}
