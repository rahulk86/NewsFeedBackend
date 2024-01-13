package com.NewFeed.backend.event.listener;


import com.NewFeed.backend.event.ForgetPasswordEvent;
import com.NewFeed.backend.modal.user.UserForgetPassword;
import com.NewFeed.backend.service.UserForgetPasswordService;
import com.NewFeed.backend.service.UserService;
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
public class ForgetPasswordEventListener implements ApplicationListener<ForgetPasswordEvent> {
    @Autowired
    private final UserService userService;

    @Autowired
    private final UserForgetPasswordService userForgetPasswordService;

    @Autowired
    private final JavaMailSender mailSender;
    @Autowired
    private TemplateEngine templateEngine;
    @Override
    public void onApplicationEvent(ForgetPasswordEvent event) {
        UserForgetPassword userForgetPassword = userForgetPasswordService.creteNewUserForgetPassword(userService.toNewFeedUser(event.getUser()));
        String url = event.getApplicationUrl(userForgetPassword.getVerificationToken());

        try {
            sendVerificationEmail(url,userForgetPassword);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("Click the link to verify your registration :  {}", url);
    }


    public void sendVerificationEmail(String url,UserForgetPassword userForgetPassword) throws MessagingException, UnsupportedEncodingException {
        String subject = "Forget Password";
        String senderName = "NewsFeed";

        Context thymeleafContext = new Context();
        String mailContent = templateEngine.process("forgetPassword.html", thymeleafContext);
        mailContent = mailContent
                        .replaceAll("userName",userForgetPassword.getUser().getName())
                        .replaceAll("url",url);

        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("rahul.tanti.software@gmail.com", senderName);
        messageHelper.setTo(userForgetPassword.getUser().getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
}