package com.eai.client_service.conf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.AttributeConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StringListConverter implements AttributeConverter<List<String>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            // Handle serialization error
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(dbData, objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
        } catch (IOException e) {
            // Handle deserialization error
            e.printStackTrace();
            return null;
        }
    }

}
