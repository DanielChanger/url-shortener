package com.example.url.shortener.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "shortened_urls")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ShortenedUrl {

    @Id
    @GeneratedValue(generator = "shortener-url-generator")
    @GenericGenerator(name = "shortener-url-generator", strategy = "com.example.url.shortener.generator.ShortenedUrlGenerator")
    private String id;
    @Column(nullable = false, unique = true)
    private String originalUrl;
    @Column(nullable = false)
    private String title;
    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ShortenedUrl that = (ShortenedUrl) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
