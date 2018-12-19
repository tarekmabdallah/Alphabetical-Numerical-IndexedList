package com.gmail.tarekmabdallah91.myindexedlist.models;

public class CategoryIndexedList extends RowInList {
    private String categoryName;
    private int index;

    public CategoryIndexedList(String text, int index) {
        this.categoryName = text;
        this.index = index;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getIndex() {
        return index;
    }

}
