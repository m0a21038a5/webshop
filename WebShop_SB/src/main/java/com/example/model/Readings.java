package com.example.model;

public class Readings {
	private String title_hira;
    private String title_kana;
    private String title_romaji;
    private String author_hira;
    private String author_kana;
    private String author_romaji;

    // 必要な getter/setter を追加
    public String getTitle_hira() {
        return title_hira;
    }

    public void setTitle_hira(String title_hira) {
        this.title_hira = title_hira;
    }

    public String getTitle_kana() {
        return title_kana;
    }

    public void setTitle_kana(String title_kana) {
        this.title_kana = title_kana;
    }

    public String getTitle_romaji() {
        return title_romaji;
    }

    public void setTitle_romaji(String title_romaji) {
        this.title_romaji = title_romaji;
    }

    public String getAuthor_hira() {
        return author_hira;
    }

    public void setAuthor_hira(String author_hira) {
        this.author_hira = author_hira;
    }

    public String getAuthor_kana() {
        return author_kana;
    }

    public void setAuthor_kana(String author_kana) {
        this.author_kana = author_kana;
    }

    public String getAuthor_romaji() {
        return author_romaji;
    }

    public void setAuthor_romaji(String author_romaji) {
        this.author_romaji = author_romaji;
    }
}
