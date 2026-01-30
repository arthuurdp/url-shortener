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

    @Column(nullable = false, unique = true)
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

    public ShortUrl(String shortKey, String originalUrl, User user) {
        this.shortKey = shortKey;
        this.originalUrl = originalUrl;
        this.createdAt = Instant.now();
        this.clicks = 0;
        this.user = user;
    }

    public ShortUrl(Long id, String shortKey, String originalUrl, Instant createdAt, Instant lastClickedAt, Integer clicks, User user) {
        this.id = id;
        this.shortKey = shortKey;
        this.originalUrl = originalUrl;
        this.createdAt = createdAt;
        this.lastClickedAt = lastClickedAt;
        this.clicks = clicks;
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

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getLastClickedAt() {
        return lastClickedAt;
    }

    public void setLastClickedAt(Instant lastClickedAt) {
        this.lastClickedAt = lastClickedAt;
    }

    public Integer getClicks() {
        return clicks;
    }

    public void setClicks(Integer clicks) {
        this.clicks = clicks;
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