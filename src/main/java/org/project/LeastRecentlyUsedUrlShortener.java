package org.project;

import org.project.encoder.IEncoder;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LeastRecentlyUsedUrlShortener implements IUrlShortener {
    private final int size;
    private final IEncoder encoder;

    private final Map<String, Url> storage = new ConcurrentHashMap<>();
    private final Queue<String> queue = new ConcurrentLinkedQueue<>();

    public LeastRecentlyUsedUrlShortener(IEncoder encoder) {
        this.size = 100;
        this.encoder = encoder;
    }

    public LeastRecentlyUsedUrlShortener(int size, IEncoder encoder) {
        this.size = size;
        this.encoder = encoder;
    }

    @Override
    public String shorten(Url url) {
        final String shortUrl = encoder.encode(url.link);

        if (storage.containsKey(shortUrl)) {
            queue.remove(shortUrl);
            return shortUrl;
        }

        if (storage.size() >= size) {
            final String toRemove = queue.poll();
            storage.remove(toRemove);
        }

        storage.put(shortUrl, url);
        queue.add(shortUrl);
        return shortUrl;
    }

    @Override
    public Url unShorten(String shortUrl) {
        if (!storage.containsKey(shortUrl)) {
            return null;
        }

        queue.remove(shortUrl);
        queue.add(shortUrl);
        return storage.get(shortUrl);
    }
}
