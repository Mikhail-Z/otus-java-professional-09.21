package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.ConstructorDetector;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

public class FileSerializer implements Serializer {

    private final String fileName;

    private final ObjectMapper objectMapper = new JsonMapper();

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        //формирует результирующий json и сохраняет его в файл
        try {
            String json = createJson(data);
            writeToFile(json);
        }
        catch (IOException e) {
            throw new FileProcessException(e);
        }
    }

    private String createJson(Map<String, Double> data) throws JsonProcessingException {
        return objectMapper.writeValueAsString(data);
    }

    private void writeToFile(String json) throws IOException {
        try (var fos = new FileOutputStream(fileName);
             var bos = new BufferedOutputStream(fos)
        ) {
            bos.write(json.getBytes(StandardCharsets.UTF_8));
        }
    }
}
