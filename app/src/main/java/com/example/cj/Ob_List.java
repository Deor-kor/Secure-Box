package com.example.cj;

public class Ob_List {

    String url;
    String name;
    String date;
    String key;

    public Ob_List() {

    }

    public Ob_List(String url,String name,String date,String key) {
        this.url=url;
        this.name=name;
        this.date=date;
        this.key=key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
