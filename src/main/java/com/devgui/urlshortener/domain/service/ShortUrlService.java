package com.devgui.urlshortener.domain.service;

import com.devgui.urlshortener.domain.exception.ShortUrlInactiveException;
import com.devgui.urlshortener.domain.exception.ShortUrlNotFoundException;
import com.devgui.urlshortener.domain.model.ShortUrl;
import com.devgui.urlshortener.infrastructure.repository.ShortUrlRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

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

    public ShortUrl incrementClicks(String shortKey){
        ShortUrl shortUrl = this.getByShortKey(shortKey);

        if (!shortUrl.isActive()) {
            throw new ShortUrlInactiveException("This short URL is inactive.");
        }

        ShortUrl updatedUrl = shortUrl.incrementClickCount();
        return shortUrlRepository.save(updatedUrl);
    }

    private ShortUrl getByShortKey(String shortKey){
        return shortUrlRepository.findByShortKey(shortKey)
                .orElseThrow(() -> new ShortUrlNotFoundException("Short URL not found for key: " + shortKey));
    }
}
