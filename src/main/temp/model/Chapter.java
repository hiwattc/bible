package com.bible.model;
import lombok.Data;
import java.util.List;

@Data
public class Chapter {
    private int chapter;
    private List<Verse> verses;
}