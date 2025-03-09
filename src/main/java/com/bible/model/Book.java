package com.bible.model;
import java.util.List;

import lombok.Data;

@Data
public class Book {
    private String book;
    private List<Chapter> chapters;
}