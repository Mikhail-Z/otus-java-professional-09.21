package ru.otus.processor;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class EvenSecondExceptionThrowProcessorTest {
    private final TimeProvider timeProviderMock = mock(TimeProvider.class);
    private final EvenSecondExceptionThrowProcessor processor = new EvenSecondExceptionThrowProcessor(timeProviderMock);

    @Test
    void process_whenOddSecond_success() {
        when(timeProviderMock.getCurrentTime()).thenReturn(NOW_WITH_ODD_SECOND);
        var message = new Message.Builder(1).build();
        var resultMessage = processor.process(message);
        assertEquals(resultMessage, message);
    }

    @Test
    void process_whenEvenSecond_throwsException() {
        when(timeProviderMock.getCurrentTime()).thenReturn(NOW_WITH_EVEN_SECOND);
        assertThrows(EvenSecondException.class,
                () -> processor.process(new Message.Builder(1).build()));
    }

    private static final LocalTime NOW_WITH_EVEN_SECOND = LocalTime.of(10, 10, 10);
    private static final LocalTime NOW_WITH_ODD_SECOND = LocalTime.of(10, 10, 11);
}
