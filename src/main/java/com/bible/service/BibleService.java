package com.bible.service;

import com.bible.model.Book;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BibleService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<String> getBookList() throws IOException {
        // UTF-8 인코딩 명시적으로 지정
        String json = Files.readString(Paths.get("src/main/resources/static/books.json"), StandardCharsets.UTF_8);
        return objectMapper.readValue(json, List.class);
    }

    // 나머지 메서드들은 그대로 유지
    public Book getBookDetails(String bookName) throws IOException {
        String json = Files.readString(Paths.get("src/main/resources/data/" + bookName + ".json"), StandardCharsets.UTF_8);
        return objectMapper.readValue(json, Book.class);
    }

    public List<String> searchBooks(String keyword) throws IOException {
        return getBookList().stream()
                .filter(book -> book.contains(keyword))
                .collect(Collectors.toList());
    }

   
}