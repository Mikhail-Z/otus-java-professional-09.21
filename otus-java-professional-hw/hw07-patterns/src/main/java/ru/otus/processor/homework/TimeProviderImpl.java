package ru.otus.processor.homework;

import java.time.LocalTime;

public class TimeProviderImpl implements TimeProvider {

    @Override
    public LocalTime getCurrentTime() {
        return LocalTime.now();
    }
}
