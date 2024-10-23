package org.date;

import lombok.Setter;
import org.project.date.IDateProvider;

import java.time.LocalDateTime;

public class MockDateProvider implements IDateProvider {
    @Setter
    public LocalDateTime dateTime;

    public MockDateProvider(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public LocalDateTime getNow() {
        return dateTime;
    }
}
