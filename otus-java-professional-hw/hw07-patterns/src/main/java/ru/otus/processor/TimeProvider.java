package ru.otus.processor;

import java.time.LocalTime;

public class TimeProvider {
    LocalTime getCurrentTime() {
        return LocalTime.now();
    }
}
