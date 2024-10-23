package org.project;

public interface IUrlShortener {
    String shorten(Url url);

    Url unShorten(String shortened);
}
