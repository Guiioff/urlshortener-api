package com.devgui.urlshortener.infrastructure.scheduler;

import com.devgui.urlshortener.infrastructure.repository.ShortUrlRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExpiredShortUrlCleanupScheduler {

    private final ShortUrlRepository shortUrlRepository;

    public ExpiredShortUrlCleanupScheduler(ShortUrlRepository shortUrlRepository) {
        this.shortUrlRepository = shortUrlRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void deactivateExpiredUrls(){
        int count = shortUrlRepository.deactivateExpiredUrls();
        System.out.println("Disabled URLs: " + count);
    }
}
