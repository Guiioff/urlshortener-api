package com.devgui.urlshortener.domain.service;

import com.devgui.urlshortener.domain.exception.ShortUrlInactiveException;
import com.devgui.urlshortener.domain.exception.ShortUrlNotFoundException;
import com.devgui.urlshortener.domain.model.ShortUrl;
import com.devgui.urlshortener.factory.ShortUrlFactory;
import com.devgui.urlshortener.infrastructure.repository.ShortUrlRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShortUrlServiceTest {

    @Mock
    private ShortUrlRepository shortUrlRepository;

    @InjectMocks
    private ShortUrlService shortUrlService;

    @DisplayName("Should create short URL when data is valid")
    @Test
    void shouldCreateShortUrlWhenDataIsValid() {
        ShortUrl shortUrl = ShortUrlFactory.create();

        when(shortUrlRepository.existsByShortKey(anyString()))
                .thenReturn(false);

        when(shortUrlRepository.save(any(ShortUrl.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ShortUrl result = shortUrlService.shorten(shortUrl);

        assertThat(result)
                .isNotNull();

        assertThat(result.getShortKey())
                .isNotBlank();

        verify(shortUrlRepository).save(any(ShortUrl.class));
    }

    @DisplayName("Should generate another key when generated key already exists")
    @Test
    void shouldGenerateAnotherKeyWhenGeneratedKeyAlreadyExists() {

        ShortUrl shortUrl = ShortUrlFactory.create();

        when(shortUrlRepository.existsByShortKey(anyString()))
                .thenReturn(true)
                .thenReturn(false);

        when(shortUrlRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ShortUrl result = shortUrlService.shorten(shortUrl);

        assertThat(result.getShortKey())
                .isNotBlank();

        verify(shortUrlRepository, times(2))
                .existsByShortKey(anyString());
    }

    @DisplayName("Should increment click count")
    @Test
    void shouldIncrementClickCount() {

        ShortUrl shortUrl = ShortUrlFactory.createComplete(
                "google.com",
                "abc123",
                null,
                10,
                true
        );

        when(shortUrlRepository.findByShortKey("abc123"))
                .thenReturn(Optional.of(shortUrl));

        when(shortUrlRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ShortUrl result = shortUrlService.incrementClicks("abc123");

        assertThat(result.getClickCount())
                .isEqualTo(11);
    }

    @DisplayName("Should throw exception when short URL is inactive")
    @Test
    void shouldThrowExceptionWhenShortUrlIsInactive() {

        ShortUrl shortUrl = ShortUrlFactory.inactive();

        when(shortUrlRepository.findByShortKey(anyString()))
                .thenReturn(Optional.of(shortUrl));

        assertThatThrownBy(() ->
                shortUrlService.incrementClicks("abc123"))
                .isInstanceOf(ShortUrlInactiveException.class);
    }

    @DisplayName("Should throw exception when short URL does not exist")
    @Test
    void shouldThrowExceptionWhenShortUrlDoesNotExist() {

        when(shortUrlRepository.findByShortKey(anyString()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                shortUrlService.incrementClicks("abc123"))
                .isInstanceOf(ShortUrlNotFoundException.class);
    }

    @DisplayName("Should return short URL by id")
    @Test
    void shouldReturnShortUrlById() {

        UUID id = UUID.randomUUID();

        ShortUrl shortUrl = ShortUrlFactory.createComplete();

        when(shortUrlRepository.findById(id))
                .thenReturn(Optional.of(shortUrl));

        ShortUrl result = shortUrlService.getById(id);

        assertThat(result)
                .isNotNull();
    }

    @DisplayName("Should throw exception when short URL id does not exist")
    @Test
    void shouldThrowExceptionWhenShortUrlIdDoesNotExist() {

        UUID id = UUID.randomUUID();

        when(shortUrlRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                shortUrlService.getById(id))
                .isInstanceOf(ShortUrlNotFoundException.class);
    }

    @DisplayName("Should return paginated short URLs")
    @Test
    void shouldReturnPaginatedShortUrls() {

        Page<ShortUrl> page = new PageImpl<>(
                List.of(
                        ShortUrlFactory.createComplete(),
                        ShortUrlFactory.createComplete()
                )
        );

        when(shortUrlRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        Page<ShortUrl> result =
                shortUrlService.getAll(0, 10);

        assertThat(result.getContent())
                .hasSize(2);
    }

    @DisplayName("Should disable short URL")
    @Test
    void shouldDisableShortUrl() {

        UUID id = UUID.randomUUID();

        ShortUrl shortUrl = ShortUrlFactory.createComplete();

        when(shortUrlRepository.findById(id))
                .thenReturn(Optional.of(shortUrl));

        shortUrlService.disable(id);

        verify(shortUrlRepository)
                .save(argThat(saved ->
                        !saved.isActive()));
    }

    @DisplayName("Should throw exception when disabling non existing short URL")
    @Test
    void shouldThrowExceptionWhenDisablingNonExistingShortUrl() {

        UUID id = UUID.randomUUID();

        when(shortUrlRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                shortUrlService.disable(id))
                .isInstanceOf(ShortUrlNotFoundException.class);
    }
}