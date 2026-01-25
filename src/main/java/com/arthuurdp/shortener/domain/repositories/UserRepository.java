package com.arthuurdp.shortener.domain.repositories;

import com.arthuurdp.shortener.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
