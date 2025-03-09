package com.bible.service;

import com.bible.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class BibleService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<Bookmark> bookmarks = new ArrayList<>();
    private List<Note> notes = new ArrayList<>();

    public List<String> getBookList() throws IOException {
        String json = new String(Files.readAllBytes(Paths.get("src/main/resources/static/books.json")));
        return objectMapper.readValue(json, List.class);
    }

    public Book getBookDetails(String bookName) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get("src/main/resources/data/" + bookName + ".json")));
        return objectMapper.readValue(json, Book.class);
    }

    public List<String> searchBooks(String keyword) throws IOException {
        return getBookList().stream()
                .filter(book -> book.contains(keyword))
                .collect(Collectors.toList());
    }

    public void addBookmark(String book, int chapter, int verse) {
        Bookmark bookmark = new Bookmark();
        bookmark.setBook(book);
        bookmark.setChapter(chapter);
        bookmark.setVerse(verse);
        bookmarks.add(bookmark);
    }

    public List<Bookmark> getBookmarks() {
        return bookmarks;
    }

    public void addNote(String book, int chapter, int verse, String content) {
        Note note = new Note();
        note.setBook(book);
        note.setChapter(chapter);
        note.setVerse(verse);
        note.setContent(content);
        notes.add(note);
    }

    public List<Note> getNotes(String book, int chapter) {
        return notes.stream()
                .filter(note -> note.getBook().equals(book) && note.getChapter() == chapter)
                .collect(Collectors.toList());
    }
}