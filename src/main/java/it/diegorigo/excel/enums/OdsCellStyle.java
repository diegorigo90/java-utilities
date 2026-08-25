package it.diegorigo.excel.enums;

import lombok.Getter;

@Getter
public enum OdsCellStyle {
    BOLD("Bold"),
    TEXT("Text");

    private String description;

    OdsCellStyle(String description) {
        this.description = description;
    }

}
