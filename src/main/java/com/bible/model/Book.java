package com.bible.model;
import java.util.List;

import lombok.Data;

@Data
public class Book {
    private String book;
    private Integer numberOfChapter;
    private Integer currentChapter;
    private Integer preChapter;
    private Integer nextChapter;
    private List<Chapter> chapters;
}