package vn.fptu.reasbe.utils.converter.vector;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Converter(autoApply = true)
public class VectorConverter implements AttributeConverter<List<Float>, String> {

    @Override
    public String convertToDatabaseColumn(List<Float> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }
        return attribute.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    @Override
    public List<Float> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(dbData.split(","))
                .map(Float::valueOf)
                .toList();
    }
}