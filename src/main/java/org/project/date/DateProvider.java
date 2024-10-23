package org.project.date;

import java.time.LocalDateTime;

public class DateProvider implements IDateProvider {
    @Override
    public LocalDateTime getNow() {
        return LocalDateTime.now();
    }
}
