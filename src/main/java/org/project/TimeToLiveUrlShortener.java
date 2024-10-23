package org.project;

import org.project.date.IDateProvider;
import org.project.encoder.IEncoder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TimeToLiveUrlShortener implements IUrlShortener {
    private final int size;
    private final IEncoder encoder;
    private final IDateProvider dateProvider;

    private final Map<String, Url> storage = new ConcurrentHashMap<>();

    public TimeToLiveUrlShortener(IEncoder encoder, IDateProvider dateProvider) {
        this.size = 100;
        this.encoder = encoder;
        this.dateProvider = dateProvider;
    }

    public TimeToLiveUrlShortener(int size, IEncoder encoder, IDateProvider dateProvider) {
        this.size = size;
        this.encoder = encoder;
        this.dateProvider = dateProvider;
    }

    @Override
    public synchronized String shorten(Url url) {
        if (storage.size() >= size) {
            final int totalRemoved = removeExpiredUrls();
            if (totalRemoved == 0) {
                System.out.println("No expired URLs to remove!");
                return null;
            }
        }

        final String shortUrl = encoder.encode(url.link);
        storage.put(shortUrl, url);
        return shortUrl;
    }

    @Override
    public Url unShorten(String shortened) {
        return storage.get(shortened);
    }

    private synchronized int removeExpiredUrls() {
        final int sizeBefore = storage.size();
        storage.entrySet().removeIf(entry -> entry.getValue().date.isBefore(dateProvider.getNow()));
        return storage.size() - sizeBefore;
    }
}
