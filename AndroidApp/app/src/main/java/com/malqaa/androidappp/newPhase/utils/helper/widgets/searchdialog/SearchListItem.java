package com.malqaa.androidappp.newPhase.utils.helper.widgets.searchdialog;



public class SearchListItem {
    int id;
    String title;
    Integer key;
    String data;

    public SearchListItem(int id, String title) {
        this.id = id;
        this.title = title;
    }
    public SearchListItem(Integer key, String title) {
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

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return title;
    }
}
