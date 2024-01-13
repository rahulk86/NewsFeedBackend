package com.NewFeed.backend.service.impl;

import com.NewFeed.backend.configuration.security.AppProperties;
import com.NewFeed.backend.exception.UserVerificationException;
import com.NewFeed.backend.modal.user.NewFeedUser;
import com.NewFeed.backend.modal.user.UserProfile;
import com.NewFeed.backend.modal.user.UserVerification;
import com.NewFeed.backend.repository.user.UserProfileRepository;
import com.NewFeed.backend.repository.user.UserRepository;
import com.NewFeed.backend.repository.user.UserVerificationRepository;
import com.NewFeed.backend.service.UserVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;

@Service
public class UserVerificationServiceImpl implements UserVerificationService {
    @Autowired
    private AppProperties appProperties;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserVerificationRepository verificationRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;
    @Override
    public void verifyByToken(String verificationToken) {
        UserVerification userVerification = verificationRepository
                                                .findByVerificationToken(verificationToken)
                                                .orElseThrow(() -> new UserVerificationException("UserVerificationException :: invalid token"));
        activateUser(userVerification.getUser());
    }

    private void activateUser(NewFeedUser user){
        if(user.getActive()==1){
            throw new UserVerificationException("UserVerificationException :: User has already been verified. Please proceed to sign in.");
        }
        user.setActive(1);
        user.setCreatAt(appProperties.now());
        NewFeedUser save = userRepository.save(user);

        UserProfile userProfile = new UserProfile();
        userProfile.setUser(save);
        userProfile.setActive(1);
        userProfile.setCreatAt(appProperties.now());
        userProfileRepository.save(userProfile);
    }

    @Override
    public void verifyByConfirmationCode(String confirmationCode) {
        UserVerification userVerification = verificationRepository
                .findByConfirmationCode(confirmationCode)
                .orElseThrow(() -> new UserVerificationException("UserVerificationException :: invalid confirmationCode"));
        activateUser(userVerification.getUser());
    }

    private static String generateVerificationCode() {
        Random random = new Random();
        int code = 100_000 + random.nextInt(900_000);
        return String.valueOf(code);
    }
    @Transactional
    @Override
    public UserVerification creteNewUserVerification(NewFeedUser user) {
        deleteUserVerification(user);
        String verificationToken = UUID.randomUUID().toString();
        String confirmationCode = generateVerificationCode();
        UserVerification userVerification = new UserVerification();
        userVerification.setActive(1);
        userVerification.setCreatAt(appProperties.now());
        userVerification.setVerificationToken(verificationToken);
        userVerification.setUser(user);
        userVerification.setConfirmationCode(confirmationCode);
        return verificationRepository.save(userVerification);
    }

    @Transactional
    @Override
    public void deleteUserVerification(NewFeedUser user) {
        verificationRepository
                .findByUser(user)
                .ifPresent(byUser -> verificationRepository.delete(byUser));
    }
}
