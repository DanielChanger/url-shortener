package com.example.url.shortener.repository;

import com.example.url.shortener.dto.OriginalUrlDto;
import com.example.url.shortener.entity.ShortenedUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ShortenedUrlRepository extends JpaRepository<ShortenedUrl, Long> {

    @Query("select new com.example.url.shortener.dto.OriginalUrlDto(s.originalUrl, s.title) " +
            "from ShortenedUrl s " +
            "where s.id = :id")
    Optional<OriginalUrlDto> findOriginalUrlById(String id);
}
