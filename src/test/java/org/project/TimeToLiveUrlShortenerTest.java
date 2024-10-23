package org.project;

import org.date.MockDateProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.project.encoder.Md5Base64Encoder;

import java.time.LocalDateTime;

public class TimeToLiveUrlShortenerTest {
    @Test
    void shortenAndUnShortenUrl() {
        final Url url = new Url("www.google.com/123", LocalDateTime.parse("2024-10-30T10:00:00"));
        TimeToLiveUrlShortener shortener = new TimeToLiveUrlShortener(
                1,
                new Md5Base64Encoder(),
                new MockDateProvider(LocalDateTime.parse("2024-10-30T10:30:00"))
        );
        final String shortened = shortener.shorten(url);
        final Url original = shortener.unShorten(shortened);
        Assertions.assertEquals(url, original);
    }

    @Test
    void DeprecateUrlsWhenMaxSize() {
        final Url url1 = new Url("www.google.com/123", LocalDateTime.parse("2024-10-30T10:00:00"));
        final Url url2 = new Url("www.google.com/456", LocalDateTime.parse("2024-10-30T11:11:11"));
        TimeToLiveUrlShortener shortener = new TimeToLiveUrlShortener(
                1,
                new Md5Base64Encoder(),
                new MockDateProvider(LocalDateTime.parse("2024-10-30T10:30:00"))
        );
        final String shortened1 = shortener.shorten(url1);
        final String shortened2 = shortener.shorten(url2);
        final Url original1 = shortener.unShorten(shortened1);
        final Url original2 = shortener.unShorten(shortened2);
        Assertions.assertNull(original1);
        Assertions.assertEquals(url2, original2);
    }

    @Test
    void CannotDeprecateUrlsWhenNoExpired() {
        final Url url1 = new Url("www.google.com/123", LocalDateTime.parse("2024-10-30T11:00:00"));
        final Url url2 = new Url("www.google.com/456", LocalDateTime.parse("2024-10-30T11:11:11"));
        TimeToLiveUrlShortener shortener = new TimeToLiveUrlShortener(
                1,
                new Md5Base64Encoder(),
                new MockDateProvider(LocalDateTime.parse("2024-10-30T10:30:00"))
        );
        final String shortened1 = shortener.shorten(url1);
        final Url original1 = shortener.unShorten(shortened1);
        Assertions.assertEquals(url1, original1);
        Assertions.assertNull(shortener.shorten(url2));
    }
}
