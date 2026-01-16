package com.arthuurdp.shortener.repositories;

import com.arthuurdp.shortener.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
