package org.project;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.project.encoder.Md5Base64Encoder;

import java.time.LocalDateTime;

public class LeastRecentlyUsedUrlShortenerTest {
    @Test
    void shortenAndUnShortenUrl() {
        final Url url = new Url("www.google.com/123", LocalDateTime.parse("2024-10-30T10:00:00"));
        LeastRecentlyUsedUrlShortener shortener = new LeastRecentlyUsedUrlShortener(1, new Md5Base64Encoder());
        final String shortened = shortener.shorten(url);
        final Url original = shortener.unShorten(shortened);
        Assertions.assertEquals(url, original);
    }

    @Test
    void ReplaceUrlWhenMaxSize() {
        final Url url1 = new Url("www.google.com/123", LocalDateTime.parse("2024-10-30T10:00:00"));
        final Url url2 = new Url("www.google.com/456", LocalDateTime.parse("2024-10-30T11:11:11"));
        LeastRecentlyUsedUrlShortener shortener = new LeastRecentlyUsedUrlShortener(1, new Md5Base64Encoder());
        final String shortened1 = shortener.shorten(url1);
        Assertions.assertEquals(url1, shortener.unShorten(shortened1));
        final String shortened2 = shortener.shorten(url2);
        final Url original1 = shortener.unShorten(shortened1);
        final Url original2 = shortener.unShorten(shortened2);
        Assertions.assertNull(original1);
        Assertions.assertEquals(url2, original2);
    }

    @Test
    void ReplaceLeastRecentlyUsedUrl() {
        final Url url1 = new Url("www.google.com/123", LocalDateTime.parse("2024-10-30T11:11:11"));
        final Url url2 = new Url("www.google.com/456", LocalDateTime.parse("2024-10-30T11:11:11"));
        final Url url3 = new Url("www.google.com/789", LocalDateTime.parse("2024-10-30T11:11:11"));
        LeastRecentlyUsedUrlShortener shortener = new LeastRecentlyUsedUrlShortener(2, new Md5Base64Encoder());
        String shortened1 = shortener.shorten(url1);
        final String shortened2 = shortener.shorten(url2);
        Assertions.assertEquals(url1, shortener.unShorten(shortened1));
        Assertions.assertEquals(url2, shortener.unShorten(shortened2));

        final String shortened3 = shortener.shorten(url3);
        Assertions.assertNull(shortener.unShorten(shortened1));
        Assertions.assertEquals(url2, shortener.unShorten(shortened2));
        Assertions.assertEquals(url3, shortener.unShorten(shortened3));
        shortened1 = shortener.shorten(url1);
        Assertions.assertEquals(url1, shortener.unShorten(shortened1));
        Assertions.assertNull(shortener.unShorten(shortened2));
        Assertions.assertEquals(url3, shortener.unShorten(shortened3));
    }
}
