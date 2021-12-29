package ru.otus.processor;

import ru.otus.model.Message;

public class EvenSecondExceptionThrowProcessor implements Processor {
    private final TimeProvider dateTimeProvider;

    public EvenSecondExceptionThrowProcessor(TimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        if (dateTimeProvider.getCurrentTime().getSecond() % 2 == 0) {
            throw new EvenSecondException();
        }

        return message;
    }
}
