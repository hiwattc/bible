package com.bible.model.memory;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Memory {
    private String theme;
    private String verse;
    private String reference;
    private String description;
}