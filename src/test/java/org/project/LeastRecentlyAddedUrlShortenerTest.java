package org.project;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.project.encoder.Md5Base64Encoder;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LeastRecentlyAddedUrlShortenerTest {
    @Test
    void shortenAndUnShortenUrl() {
        final Url url = new Url("www.google.com/123", LocalDateTime.parse("2024-10-30T10:00:00"));
        LeastRecentlyAddedUrlShortener shortener = new LeastRecentlyAddedUrlShortener(1, new Md5Base64Encoder());
        final String shortened = shortener.shorten(url);
        final Url original = shortener.unShorten(shortened);
        Assertions.assertEquals(url, original);
    }

    @Test
    void ReplaceUrlWhenMaxSize() {
        final Url url1 = new Url("www.google.com/123", LocalDateTime.parse("2024-10-30T10:00:00"));
        final Url url2 = new Url("www.google.com/456", LocalDateTime.parse("2024-10-30T11:11:11"));
        LeastRecentlyAddedUrlShortener shortener = new LeastRecentlyAddedUrlShortener(1, new Md5Base64Encoder());
        final String shortened1 = shortener.shorten(url1);
        final String shortened2 = shortener.shorten(url2);
        final Url original1 = shortener.unShorten(shortened1);
        final Url original2 = shortener.unShorten(shortened2);
        Assertions.assertNull(original1);
        Assertions.assertEquals(url2, original2);
    }


    @Test
    void testConcurrentShortening() throws InterruptedException {
        final int numThreads = 20;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        LeastRecentlyAddedUrlShortener shortener = new LeastRecentlyAddedUrlShortener(10, new Md5Base64Encoder());

        for (int i = 0; i < numThreads; i++) {
            final int index = i;
            executor.submit(() -> {
                final Url url = new Url("www.google.com" + index, LocalDateTime.parse("2024-10-30T11:11:" + index));
                final String shortened = shortener.shorten(url);
                Assertions.assertNotNull(shortened);
                Assertions.assertNotNull(shortener.unShorten(shortened));
            });
        }

        executor.shutdown();
        //noinspection ResultOfMethodCallIgnored
        executor.awaitTermination(5, TimeUnit.SECONDS);
        Assertions.assertEquals(10, shortener.getSize());
    }
}
