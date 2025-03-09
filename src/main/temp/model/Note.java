// Note.java (새로 추가)
package com.bible.model;
import lombok.Data;

@Data
public class Note {
    private String book;
    private int chapter;
    private int verse;
    private String content;
}