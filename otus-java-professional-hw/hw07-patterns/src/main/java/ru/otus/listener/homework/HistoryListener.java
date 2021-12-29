package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {
    private final List<Message> messages = new ArrayList<>();

    @Override
    public void onUpdated(Message msg) {
        messages.add((Message) msg.deepCopy());
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return messages.stream().filter(msg -> msg.getId() == id).findFirst();
    }
}
