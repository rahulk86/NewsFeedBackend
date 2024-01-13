package com.NewFeed.backend.event.listener;


import com.NewFeed.backend.event.RegistrationCompleteEvent;
import com.NewFeed.backend.modal.user.UserVerification;
import com.NewFeed.backend.service.UserService;
import com.NewFeed.backend.service.UserVerificationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    @Autowired
    private final UserService userService;

    @Autowired
    private final UserVerificationService userVerificationService;

    @Autowired
    private final JavaMailSender mailSender;
    @Autowired
    private TemplateEngine templateEngine;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        UserVerification userVerification = userVerificationService.creteNewUserVerification(userService.toNewFeedUser(event.getUser()));
        String url = event.getApplicationUrl(userVerification.getVerificationToken());

        try {
            sendVerificationEmail(url,userVerification);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("Click the link to verify your registration :  {}", url);
    }


    public void sendVerificationEmail(String url,UserVerification userVerification) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "NewsFeed";

        Context thymeleafContext = new Context();
        String mailContent = templateEngine.process("verification-email.html", thymeleafContext);
        mailContent = mailContent
                        .replaceAll("userName",userVerification.getUser().getName())
                        .replaceAll("url",url)
                        .replaceAll("confirmationCode",userVerification.getConfirmationCode());
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("rahul.tanti.software@gmail.com", senderName);
        messageHelper.setTo(userVerification.getUser().getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
}