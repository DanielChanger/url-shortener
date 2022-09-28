package com.example.url.shortener.service;

import com.example.url.shortener.dto.OriginalUrlDto;
import com.example.url.shortener.entity.ShortenedUrl;
import com.example.url.shortener.repository.ShortenedUrlRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final ShortenedUrlRepository shortenedUrlRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Retryable(maxAttempts = 5)
    public String shortenUrl(OriginalUrlDto originalUrlDto) {
        ShortenedUrl shortenedUrl = modelMapper.map(originalUrlDto, ShortenedUrl.class);
        shortenedUrlRepository.save(shortenedUrl);
        return shortenedUrl.getId();
    }

    @Cacheable("urls")
    public Optional<String> getOriginalUrl(String shortenedUrlId) {
        return shortenedUrlRepository.findOriginalUrlById(shortenedUrlId).map(OriginalUrlDto::getOriginalUrl);
    }
}
