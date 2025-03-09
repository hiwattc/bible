package com.bible.service;

import com.bible.model.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BibleService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<String> getBookList() throws IOException {
        // 클래스패스에서 books.json 읽기
        ClassPathResource resource = new ClassPathResource("data/books.json");
        try (InputStream inputStream = resource.getInputStream()) {
            String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return objectMapper.readValue(json, List.class);
        }
    }

    public Book getBookDetails(String bookName) throws IOException {
        // 클래스패스에서 bookName.json 읽기
        ClassPathResource resource = new ClassPathResource("data/" + bookName + ".json");
        try (InputStream inputStream = resource.getInputStream()) {
            String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return objectMapper.readValue(json, Book.class);
        }
    }

    public List<String> searchBooks(String keyword) throws IOException {
        return getBookList().stream()
                .filter(book -> book.contains(keyword))
                .collect(Collectors.toList());
    }
}