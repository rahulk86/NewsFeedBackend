package com.NewFeed.backend.repository;

import com.NewFeed.backend.modal.ERole;
import com.NewFeed.backend.modal.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
