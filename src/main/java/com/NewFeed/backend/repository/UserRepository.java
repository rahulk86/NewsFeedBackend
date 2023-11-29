package com.NewFeed.backend.repository;


import com.NewFeed.backend.modal.NewFeedUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface UserRepository extends JpaRepository <NewFeedUser,Long> {
    public Optional<NewFeedUser> findByEmail(String email);
    public Optional<NewFeedUser> findByName(String name);

    public boolean existsByEmail(String email);

    public boolean existsByName(String name);
}
