package com.bible.controller;

import com.bible.model.memory.Memory;
import com.bible.service.BibleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@Controller
public class MemoryController {
    @Autowired
    private BibleService bibleService;

    private static final Logger logger = LoggerFactory.getLogger(MemoryController.class);
    @GetMapping("/memory/list")
    public String index(Model model) throws Exception {
        model.addAttribute("memoryList", bibleService.getBookList());
        return "index";
    }
    @GetMapping("/memory/{memoryName}")
    public String getBook(@PathVariable String memoryName, 
                         Model model) throws Exception {
        List<Memory> memoryCardList = bibleService.getMemoryDetails(memoryName);
        model.addAttribute("memoryName", memoryName);
        model.addAttribute("memoryCardList", memoryCardList);
        return "memory/memory";
    }

}