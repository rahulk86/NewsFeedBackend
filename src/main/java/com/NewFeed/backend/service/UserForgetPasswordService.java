package com.NewFeed.backend.service;

import com.NewFeed.backend.modal.user.NewFeedUser;
import com.NewFeed.backend.modal.user.UserForgetPassword;
import com.NewFeed.backend.payload.Request.ForgetPasswordRequest;

public interface UserForgetPasswordService {
    void verifyByToken(ForgetPasswordRequest passwordRequest);
    UserForgetPassword creteNewUserForgetPassword(NewFeedUser user);

    void deleteUserForgetPassword(NewFeedUser user);
}
