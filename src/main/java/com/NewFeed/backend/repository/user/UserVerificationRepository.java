package com.NewFeed.backend.repository.user;

import com.NewFeed.backend.modal.user.NewFeedUser;
import com.NewFeed.backend.modal.user.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserVerificationRepository extends JpaRepository<UserVerification,Long> {

    Optional<UserVerification> findByVerificationToken(String verificationToken);
    Optional<UserVerification> findByConfirmationCode(String confirmationCode);
    Optional<UserVerification> findByUser(NewFeedUser user);
}
