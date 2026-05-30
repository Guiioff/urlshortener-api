package com.devgui.urlshortener.infrastructure.repository;

import com.devgui.urlshortener.domain.model.ShortUrl;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, UUID> {

    Optional<ShortUrl> findByShortKey(String shortKey);

    boolean existsByShortKey(String shortKey);

    @Transactional
    @Modifying
    @Query("""
        UPDATE ShortUrl su
        SET su.active = false
        WHERE su.expiresAt < CURRENT_TIMESTAMP AND su.active = true
    """)
    int deactivateExpiredUrls();
}
