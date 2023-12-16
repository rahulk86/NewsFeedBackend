package com.NewFeed.backend.repository.auth;

import com.NewFeed.backend.modal.auth.AuthProvider;
import com.NewFeed.backend.modal.user.NewFeedUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthProviderRepository extends JpaRepository<AuthProvider,Long> {
    Optional<AuthProvider> findByUser(NewFeedUser user);

}
