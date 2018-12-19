package com.gmail.tarekmabdallah91.myindexedlist.models;

public class SectionIndexedList extends RowInList {
    private final String text;

    public SectionIndexedList(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}