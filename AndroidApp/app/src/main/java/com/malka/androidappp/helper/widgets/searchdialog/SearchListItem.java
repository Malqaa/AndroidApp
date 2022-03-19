package com.malka.androidappp.helper.widgets.searchdialog;

/**
 * Created by ajithvgiri on 06/11/17.
 */

public class SearchListItem {
    int id;
    String title;
    String key;
    String data;

    public SearchListItem(int id, String title) {
        this.id = id;
        this.title = title;
    }
    public SearchListItem(String key, String title) {
        this.key = key;
        this.title = title;
    }



    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return title;
    }
}
