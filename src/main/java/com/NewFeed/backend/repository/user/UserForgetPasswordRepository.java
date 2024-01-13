package com.NewFeed.backend.repository.user;

import com.NewFeed.backend.modal.user.NewFeedUser;
import com.NewFeed.backend.modal.user.UserForgetPassword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserForgetPasswordRepository extends JpaRepository<UserForgetPassword,Long> {

    Optional<UserForgetPassword> findByVerificationToken(String verificationToken);
    Optional<UserForgetPassword> findByUser(NewFeedUser user);
}
