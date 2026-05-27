package com.devgui.urlshortener.infrastructure.repository;

import com.devgui.urlshortener.domain.model.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, UUID> {

    Optional<ShortUrl> findByShortKey(String shortKey);

    boolean existsByShortKey(String shortKey);
}
