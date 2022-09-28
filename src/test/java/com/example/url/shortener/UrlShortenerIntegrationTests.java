package com.example.url.shortener;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.GenericContainer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class UrlShortenerIntegrationTests {

    private static GenericContainer<?> shortenedUrlDbContainer;
    private static String baseUrl;
    private MockMvc mvc;

    @BeforeAll
    static void beforeAll() {
        baseUrl = "/short";
        shortenedUrlDbContainer = new GenericContainer<>("postgres");
        shortenedUrlDbContainer.start();
    }

    @BeforeEach
    void setUp(WebApplicationContext context) {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @AfterAll
    static void afterAll() {
        shortenedUrlDbContainer.stop();
    }

    @Test
    @DisplayName("Shortens url and redirects to original url")
    void shortenUrlHappyPath() throws Exception {
        String originalUrl = "https://github.com/";
        String json = ("""
                {
                  "url": "%s",
                  "title": "Github"
                }""").formatted(originalUrl);

        String location = mvc.perform(post(baseUrl).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"))
                .andReturn().getResponse()
                .getHeader(HttpHeaders.LOCATION);

        mvc.perform(get(location))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("location", originalUrl));
    }
}
