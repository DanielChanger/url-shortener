package com.example.url.shortener.controller;

import com.example.url.shortener.dto.OriginalUrlDto;
import com.example.url.shortener.service.UrlShortenerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/short")
@RequiredArgsConstructor
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    @PostMapping
    public ResponseEntity<Object> shortenUrl(@RequestBody OriginalUrlDto originalUrlDto) {
        String shortenedUrlId = urlShortenerService.shortenUrl(originalUrlDto);
        URI redirectUrl = buildRedirectUrl(shortenedUrlId);
        return ResponseEntity.created(redirectUrl).build();
    }

    private static URI buildRedirectUrl(String shortenedUrl) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .pathSegment("{shortenedUrlId}")
                .buildAndExpand(shortenedUrl)
                .toUri();
    }

    @GetMapping("{shortenedUrlId}")
    public ResponseEntity<Object> redirectToOriginalUrl(@PathVariable String shortenedUrlId) {
        Optional<String> originalUrl = urlShortenerService.getOriginalUrl(shortenedUrlId);

        if (originalUrl.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        URI redirectUrl = URI.create(originalUrl.get());
        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT)
                .location(redirectUrl)
                .build();
    }
}
