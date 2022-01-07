package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.ConstructorDetector;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import ru.otus.model.Measurement;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ResourcesFileLoader implements Loader {

    private final String fileName;
    private final ObjectMapper objectMapper = new JsonMapper();

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        //читает файл, парсит и возвращает результат
        var file = getFile();
        return deserialize(file);
    }

    private File getFile() {
        try {
            URL res = getClass().getClassLoader().getResource(fileName);
            return Paths.get(res.toURI()).toFile();
        } catch (Exception e) {
            throw new FileProcessException(e);
        }
    }

    private List<Measurement> deserialize(File file) {
        ArrayNode arrayNode;
        try {
            arrayNode = (ArrayNode)objectMapper.readTree(file);
        } catch (IOException e) {
            throw new FileProcessException(e);
        }

        var measurements = new ArrayList<Measurement>(arrayNode.size());
        for (var object : arrayNode) {
            var name = object.get("name").asText();
            var value = object.get("value").asDouble();
            measurements.add(new Measurement(name, value));
        }

        return measurements;
    }
}
