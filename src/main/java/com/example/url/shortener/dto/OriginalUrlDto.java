package com.example.url.shortener.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Value;

@Value
public class OriginalUrlDto {
    @JsonAlias("url")
    String originalUrl;
    String title;
}
