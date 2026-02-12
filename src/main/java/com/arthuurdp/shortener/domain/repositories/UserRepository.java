package com.arthuurdp.shortener.domain.repositories;

import com.arthuurdp.shortener.domain.entities.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.shortUrls WHERE u.id = :id")
    Optional<User> findByIdWithShortUrls(@Param("id") Long id);

    UserDetails findByEmail(String email);
}
