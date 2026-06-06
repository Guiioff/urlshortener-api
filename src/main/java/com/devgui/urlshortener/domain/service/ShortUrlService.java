package com.devgui.urlshortener.domain.service;

import com.devgui.urlshortener.domain.exception.ShortUrlInactiveException;
import com.devgui.urlshortener.domain.exception.ShortUrlNotFoundException;
import com.devgui.urlshortener.domain.model.ShortUrl;
import com.devgui.urlshortener.infrastructure.repository.ShortUrlRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;

    public ShortUrlService(ShortUrlRepository shortUrlRepository) {
        this.shortUrlRepository = shortUrlRepository;
    }

    public ShortUrl shorten(ShortUrl shortUrl) {
        String shortKey;
        do {
            shortKey = RandomStringUtils.secure().nextAlphanumeric(5, 10);
        } while (shortUrlRepository.existsByShortKey(shortKey));

        ShortUrl urlToSave = shortUrl.withShortKey(shortKey);
        return shortUrlRepository.save(urlToSave);
    }

    @CacheEvict(value = "shortUrls", key = "#result.id")
    public ShortUrl incrementClicks(String shortKey){
        ShortUrl shortUrl = this.getByShortKey(shortKey);

        if (!shortUrl.isActive()) {
            throw new ShortUrlInactiveException("This short URL is inactive.");
        }

        ShortUrl updatedUrl = shortUrl.incrementClickCount();
        return shortUrlRepository.save(updatedUrl);
    }

    @Cacheable(value = "shortUrls", key = "#id")
    public ShortUrl getById(UUID id){
        return this.findByIdOrThrow(id);
    }

    public Page<ShortUrl> getAll(Integer page, Integer size){
        Pageable pageRequest = PageRequest.of(page, size);
        return shortUrlRepository.findAll(pageRequest);
    }

    @CacheEvict(value = "shortUrls", key = "#id")
    public void disable(UUID id){
        ShortUrl shortUrl = this.findByIdOrThrow(id);
        ShortUrl updatedUrl = shortUrl.disable();
        shortUrlRepository.save(updatedUrl);
    }

    private ShortUrl getByShortKey(String shortKey){
        return shortUrlRepository.findByShortKey(shortKey)
                .orElseThrow(() -> new ShortUrlNotFoundException("Short URL not found for key: " + shortKey));
    }

    private ShortUrl findByIdOrThrow(UUID id) {
        return shortUrlRepository.findById(id)
                .orElseThrow(() -> new ShortUrlNotFoundException("Short URL not found for id: " + id));
    }
}
