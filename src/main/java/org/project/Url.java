package org.project;

import java.time.LocalDateTime;

public class Url {
    public final String link;
    public final LocalDateTime date;

    public Url(String link, LocalDateTime date) {
        this.link = link;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Url{" + link + "," + date + "}";
    }
}
