package com.arthuurdp.shortener.domain.entities.url;

import com.arthuurdp.shortener.domain.entities.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "short_urls")
public class ShortUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String shortKey;

    @Column(nullable = false)
    private String originalUrl;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant lastClickedAt;

    @Column(nullable = false)
    private Integer clicks;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public ShortUrl() {
    }

    public ShortUrl(String originalUrl, User user) {
        this.originalUrl = originalUrl;
        this.createdAt = Instant.now();
        this.clicks = 0;
        this.user = user;
    }


    public Long getId() {
        return id;
    }

    public String getShortKey() {
        return shortKey;
    }

    public void setShortKey(String shortKey) {
        this.shortKey = shortKey;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getLastClickedAt() {
        return lastClickedAt;
    }

    public Integer getClicks() {
        return clicks;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void incrementClicks() {
        this.clicks++;
        this.lastClickedAt = Instant.now();
    }
}