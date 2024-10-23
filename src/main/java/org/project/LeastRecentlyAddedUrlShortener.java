package org.project;

import lombok.Getter;
import org.project.encoder.IEncoder;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LeastRecentlyAddedUrlShortener implements IUrlShortener {
    public static String URL_BASE = "short.url/";

    @Getter
    private final int size;
    private final IEncoder encoder;
    private final Map<String, Url> storage = new ConcurrentHashMap<>();
    private final Queue<String> queue = new ConcurrentLinkedQueue<>();

    public LeastRecentlyAddedUrlShortener(IEncoder encoder) {
        this.size = 100;
        this.encoder = encoder;
    }

    public LeastRecentlyAddedUrlShortener(int size, IEncoder encoder) {
        this.size = size;
        this.encoder = encoder;
    }

    @Override
    public synchronized String shorten(Url url) {
        if (storage.size() >= size) {
            final String toRemove = queue.poll();
            storage.remove(toRemove);
        }

        final String shortened = URL_BASE + encoder.encode(url.link);
        if (storage.containsKey(shortened)) {
            queue.remove(shortened);
        }

        storage.put(shortened, url);
        queue.add(shortened);

        return shortened;
    }

    @Override
    public Url unShorten(String shortened) {
        return storage.get(shortened);
    }
}
