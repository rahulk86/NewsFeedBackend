package com.NewFeed.backend.repository;

import com.NewFeed.backend.modal.AuthProvider;
import com.NewFeed.backend.modal.NewFeedUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthProviderRepository extends JpaRepository<AuthProvider,Long> {
    Optional<AuthProvider> findByUser(NewFeedUser user);

}
