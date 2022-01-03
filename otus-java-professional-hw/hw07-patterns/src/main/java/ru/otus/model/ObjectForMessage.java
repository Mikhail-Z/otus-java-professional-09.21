package ru.otus.model;

import ru.otus.processor.homework.Copyable;

import java.util.ArrayList;
import java.util.List;

public class ObjectForMessage implements Copyable<ObjectForMessage> {
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public ObjectForMessage deepCopy() {
        var objectForMessage = new ObjectForMessage();
        objectForMessage.data = new ArrayList<>(data);
        return objectForMessage;
    }
}
