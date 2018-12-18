package com.gmail.tarekmabdallah91.myindexedlist.models;

import java.util.ArrayList;
import java.util.List;

public class Section {

    private String name ;
    private int id ;
    private List<ListItem> childes = new ArrayList<>();

    public Section(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public List<ListItem> getChildes() {
        return childes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setChildes(List<ListItem> childes) {
        this.childes = childes;
    }
}
