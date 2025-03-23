package com.bible.service;

import com.bible.model.Book;
import com.bible.model.memory.Memory;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BibleService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(BibleService.class);

    public List<String> getBookList() throws IOException {
        // 클래스패스에서 books.json 읽기
        ClassPathResource resource = new ClassPathResource("data/books.json");
        try (InputStream inputStream = resource.getInputStream()) {
            String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return objectMapper.readValue(json, List.class);
        }
    }
    public List<String> getOldBookList() throws IOException {
        // 클래스패스에서 books.json 읽기
        ClassPathResource resource = new ClassPathResource("data/books_old.json");
        try (InputStream inputStream = resource.getInputStream()) {
            String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return objectMapper.readValue(json, List.class);
        }
    }
    public List<String> getNewBookList() throws IOException {
        // 클래스패스에서 books.json 읽기
        ClassPathResource resource = new ClassPathResource("data/books_new.json");
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
    public List<String> searchOldBooks(String keyword) throws IOException {
        return getOldBookList().stream()
                .filter(book -> book.contains(keyword))
                .collect(Collectors.toList());
    }
    public List<String> searchNewBooks(String keyword) throws IOException {
        return getNewBookList().stream()
                .filter(book -> book.contains(keyword))
                .collect(Collectors.toList());
    }

    public List<String> getMemoryList() throws IOException {
        ClassPathResource resource = new ClassPathResource("memory/index.json");
        try (InputStream inputStream = resource.getInputStream()) {
            String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return objectMapper.readValue(json, List.class);
        }
    }
    public List<Memory> getMemoryDetails(String memoryName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Memory> data = null;
        try {
            // ClassPathResource를 사용해 resources 폴더에서 파일 로드
            ClassPathResource resource = new ClassPathResource("memory/" + memoryName + ".json");
            Memory[] dataArray = mapper.readValue(resource.getInputStream(), Memory[].class);
            data = Arrays.asList(dataArray);
        } catch (IOException e) {
            logger.error("Failed to load JSON file: {}", memoryName, e);
        }
        return data;
    }

}