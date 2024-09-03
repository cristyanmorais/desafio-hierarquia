package com.desafio.hierarquia.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class HierarchyService {
    private static final String FILE_PATH = "src/main/java/com/desafio/hierarquia/dicts/hierarquia.json";
    private Map<String, Object> hierarchyData = new HashMap<>();

    public Map<String, Object> getHierarchy() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            hierarchyData = mapper.readValue(new File(FILE_PATH), Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return hierarchyData;
    }

    public void saveHierarchy(Map<String, Object> hierarchy) {
        this.hierarchyData = hierarchy;

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(FILE_PATH), hierarchyData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] generateJsonFile() {
        try {
            return Files.readAllBytes(Paths.get(FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new byte[0];
    }
}
