package ru.otus.model;

import ru.otus.processor.Copyable;

import java.util.ArrayList;
import java.util.List;

public class ObjectForMessage implements Copyable {
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public Copyable deepCopy() {
        var objectForMessage = new ObjectForMessage();
        objectForMessage.data = new ArrayList<>(data);
        return objectForMessage;
    }
}
