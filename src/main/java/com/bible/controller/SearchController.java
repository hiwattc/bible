package com.bible.controller;

// ... 기존 import ...
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import java.nio.file.Files;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.bible.model.Book;
import com.bible.model.Chapter;
import com.bible.model.Verse;
import com.bible.service.BibleService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Data;

@Controller
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
    private static final int PAGE_SIZE = 10; // 페이지당 결과 수

    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private BibleService bibleService;

    @GetMapping("/search")
    public String searchContent(
            @RequestParam String keyword,
            @RequestParam(required = false) boolean contentSearch,
            @RequestParam(defaultValue = "0") int page,
            Model model) throws Exception {

        model.addAttribute("contentSearch", contentSearch);

        if (contentSearch) {
            List<VerseResult> results = new ArrayList<>();
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:data/*.json");
            
            // book으로 시작하는 파일 제외
            resources = Arrays.stream(resources)
                .filter(resource -> {
                    try {
                        String filename = resource.getFilename();
                        return filename != null && !filename.startsWith("book");
                    } catch (Exception e) {
                        logger.error("파일 이름 확인 중 오류 발생", e);
                        return false;
                    }
                })
                .toArray(Resource[]::new);

            ObjectMapper objectMapper = new ObjectMapper();

            for (Resource resource : resources) {
                try (InputStream inputStream = resource.getInputStream()) {
                    String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Book book = objectMapper.readValue(json, Book.class);

                    String bookName = book.getBook();
                    logger.info("bookName::"+bookName);
                    logger.info("keyword::"+keyword);
                    for (Chapter chapter : book.getChapters()) {
                        int chapterNum = chapter.getChapter();
                        for (Verse verse : chapter.getVerses()) {
                            if (verse.getText() != null && verse.getText().contains(keyword)) {
                                results.add(new VerseResult(bookName, chapterNum, verse.getVerse(), verse.getText(), keyword));
                            }
                        }
                    }
                }
            }

            // 페이징 처리
            int start = page * PAGE_SIZE;
            int end = Math.min(start + PAGE_SIZE, results.size());
            List<VerseResult> pagedResults = results.subList(start, end);
            
            Pageable pageable = PageRequest.of(page, PAGE_SIZE);
            Page<VerseResult> resultPage = new PageImpl<>(pagedResults, pageable, results.size());

            model.addAttribute("searchResults", resultPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", resultPage.getTotalPages());
            model.addAttribute("keyword", keyword);
        } else {
            // 기존 제목/책 검색 로직
            model.addAttribute("books_old", bibleService.searchOldBooks(keyword));
            model.addAttribute("books_new", bibleService.searchNewBooks(keyword));
            return "index";
        }
        return "index";
    }


    @Data
    public static class VerseResult {
        private String book;
        private int chapter;
        private int verse;
        private String text;
        private String keyword;

        // 생성자, getter, setter
        public VerseResult(String book, int chapter, int verse, String text, String keyword) {
            this.book = book;
            this.chapter = chapter;
            this.verse = verse;
            this.text = text;
            this.keyword = keyword;
        }
    }
}