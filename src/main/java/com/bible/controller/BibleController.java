package com.bible.controller;

import com.bible.model.Book;
import com.bible.service.BibleService;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(BibleController.class);

    @GetMapping("/")
    public String index(Model model) throws Exception {
        //model.addAttribute("books", bibleService.getBookList());
        model.addAttribute("memoryList", bibleService.getMemoryList());
        model.addAttribute("books_old", bibleService.getOldBookList());
        model.addAttribute("books_new", bibleService.getNewBookList());
        return "index";
    }

    @GetMapping("/book/{bookName}")
    public String getBook(@PathVariable String bookName, 
                         @RequestParam(required = false) Integer chapter,
                         Model model) throws Exception {
        Book book = bibleService.getBookDetails(bookName);
        book.setNumberOfChapter(book.getChapters().size());
        if (chapter != null) {
            logger.info("chapter:"+chapter);
            logger.info("book.getNumberOfChapter():"+book.getNumberOfChapter());
            
            book.setCurrentChapter(chapter);
            book.setPreChapter(chapter-1==0?null:chapter-1);
            book.setNextChapter(chapter+1>book.getNumberOfChapter()?null:chapter+1);
            book.setChapters(book.getChapters().stream()
                    .filter(ch -> ch.getChapter() == chapter)
                    .collect(Collectors.toList()));
        }
        model.addAttribute("book", book);
        return "book";
    }

    @GetMapping("/search")
    public String search(@RequestParam String keyword, Model model) throws Exception {
        model.addAttribute("books_old", bibleService.searchOldBooks(keyword));
        model.addAttribute("books_new", bibleService.searchNewBooks(keyword));
        return "index";
    }


}