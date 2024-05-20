package com.opd.phonenumberapi;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Autowired;

@Converter
public class StringCryptConverter implements AttributeConverter<String, String> {

    @Autowired
    DataCryptService dataCryptService;

    @Override
    public String convertToDatabaseColumn(String s) {
        try {
            return dataCryptService.encrypt(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String s) {
        try {
            return dataCryptService.decrypt(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
