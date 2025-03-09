package com.bible.controller;

import com.bible.model.Book;
import com.bible.service.BibleService;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BibleController {
    @Autowired
    private BibleService bibleService;

    @GetMapping("/")
    public String index(Model model) throws Exception {
        model.addAttribute("books", bibleService.getBookList());
        return "index";
    }

    @GetMapping("/book/{bookName}")
    public String getBook(@PathVariable String bookName, 
                         @RequestParam(required = false) Integer chapter,
                         Model model) throws Exception {
        Book book = bibleService.getBookDetails(bookName);
        if (chapter != null) {
            book.setChapters(book.getChapters().stream()
                    .filter(ch -> ch.getChapter() == chapter)
                    .collect(Collectors.toList()));
        }
        model.addAttribute("book", book);
        return "book";
    }

    @GetMapping("/search")
    public String search(@RequestParam String keyword, Model model) throws Exception {
        model.addAttribute("books", bibleService.searchBooks(keyword));
        return "index";
    }


}