package com.spsa.strategy.config;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.jsoup.nodes.Entities;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class SanitizedStringDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String originalValue = p.getText();
        originalValue = Jsoup.clean(originalValue, Safelist.basic()); // script check
        originalValue = Entities.escape(originalValue); // html check
        return originalValue;
    }
}