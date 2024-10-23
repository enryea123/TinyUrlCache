package org.project;

/*
 * Write a URL shortener, which doesn't allow more than 100 URLs to be stored.
 *
 * we need to encode, decode.
 * make sure <= 100
 * make sure no duplicate URLs
 * need to create unique shortened ids
 * we can have different strategies for generating Ids:
 *      hash, increasing ids
 * we can have different strategies for replacing URLs when we get to 100:
 *      random, least recently added, LRU, LFU
 *
 * thread safe? TDD?
 *
 */

import org.project.date.DateProvider;
import org.project.encoder.Md5Base64Encoder;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        IUrlShortener shortener = new LeastRecentlyAddedUrlShortener(new Md5Base64Encoder());
        String shortUrl = shortener.shorten(
                new Url("google.com/123456asdkja/kasjdsk/asldasdlk", LocalDateTime.now()));
        System.out.println("ShortUrl: " + shortUrl);
        System.out.println("Original: " + shortener.unShorten(shortUrl));

        shortener = new TimeToLiveUrlShortener(new Md5Base64Encoder(), new DateProvider());
        shortUrl = shortener.shorten(
                new Url("google.com/123456asdkja/kasjdsk/asldasdlk", LocalDateTime.now()));
        System.out.println("ShortUrl: " + shortUrl);
        System.out.println("Original: " + shortener.unShorten(shortUrl));

        shortener = new LeastRecentlyUsedUrlShortener(new Md5Base64Encoder());
        shortUrl = shortener.shorten(
                new Url("google.com/123456asdkja/kasjdsk/asldasdlk", LocalDateTime.now()));
        System.out.println("ShortUrl: " + shortUrl);
        System.out.println("Original: " + shortener.unShorten(shortUrl));
    }
}
