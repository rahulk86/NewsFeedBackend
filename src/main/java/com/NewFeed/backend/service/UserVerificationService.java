package com.NewFeed.backend.service;

import com.NewFeed.backend.modal.user.NewFeedUser;
import com.NewFeed.backend.modal.user.UserVerification;

public interface UserVerificationService {
    void verifyByToken(String verificationToken);
    void verifyByConfirmationCode(String confirmationCode);
    UserVerification creteNewUserVerification(NewFeedUser user);

    void deleteUserVerification(NewFeedUser user);
}
