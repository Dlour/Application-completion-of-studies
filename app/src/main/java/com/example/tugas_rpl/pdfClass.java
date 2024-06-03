package com.example.tugas_rpl;

public class pdfClass {


    public String name;
    public String url;

    public pdfClass(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public pdfClass() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
