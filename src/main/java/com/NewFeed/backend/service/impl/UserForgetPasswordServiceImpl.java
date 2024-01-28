package com.NewFeed.backend.service.impl;

import com.NewFeed.backend.configuration.security.AppProperties;
import com.NewFeed.backend.modal.user.NewFeedUser;
import com.NewFeed.backend.modal.user.UserForgetPassword;
import com.NewFeed.backend.payload.Request.ForgetPasswordRequest;
import com.NewFeed.backend.repository.user.UserForgetPasswordRepository;
import com.NewFeed.backend.repository.user.UserRepository;
import com.NewFeed.backend.service.UserForgetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserForgetPasswordServiceImpl implements UserForgetPasswordService {
    @Autowired
    private AppProperties appProperties;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserForgetPasswordRepository userForgetPasswordRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void verifyByToken(ForgetPasswordRequest passwordRequest) {
        UserForgetPassword userForgetPassword = userForgetPasswordRepository
                .findByVerificationToken(passwordRequest.getToken())
                .orElseThrow(() -> new DataIntegrityViolationException("DataIntegrityViolationException :: Invalid reset password URL. Please request a new reset link."));
        NewFeedUser user = userForgetPassword.getUser();
        user.setPassword(passwordEncoder.encode(passwordRequest.getPassword()));
        user.setCreatAt(appProperties.now());
        deleteUserForgetPassword(userRepository.save(user));
    }



    @Transactional
    @Override
    public UserForgetPassword creteNewUserForgetPassword(NewFeedUser user) {
        deleteUserForgetPassword(user);
        String verificationToken = UUID.randomUUID().toString();
        UserForgetPassword forgetPassword = new UserForgetPassword();
        forgetPassword.setActive(1);
        forgetPassword.setCreatAt(appProperties.now());
        forgetPassword.setVerificationToken(verificationToken);
        forgetPassword.setUser(user);
        return userForgetPasswordRepository.save(forgetPassword);
    }

    @Transactional
    @Override
    public void deleteUserForgetPassword(NewFeedUser user) {
        userForgetPasswordRepository
                .findByUser(user)
                .ifPresent(byUser -> userForgetPasswordRepository.delete(byUser));
    }
}
