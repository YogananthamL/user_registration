package com.yogan.user_registration.models;

import jakarta.persistence.*;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name="token")
@Builder
public class Token {
    @Id
    @GeneratedValue
    @Column(name="token_id")
    private int id;
    @Column(name="token")
    private String token;
    @Column(name="created_at")
    private LocalDateTime createdAt;
    @Column(name="expired_at")
    private LocalDateTime expiredAt;
    @Column(name="validated_at")
    private LocalDateTime validatedAt;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private ApplicationUser user;

    public Token(int id, String token, LocalDateTime createdAt, LocalDateTime expiredAt, LocalDateTime validatedAt, ApplicationUser user) {
        this.id = id;
        this.token = token;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
        this.validatedAt = validatedAt;
        this.user = user;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public Token() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    public LocalDateTime getValidatedAt() {
        return validatedAt;
    }

    public void setValidatedAt(LocalDateTime validatedAt) {
        this.validatedAt = validatedAt;
    }

    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", createdAt=" + createdAt +
                ", expiredAt=" + expiredAt +
                ", validatedAt=" + validatedAt +
                ", user=" + user +
                '}';
    }
}
