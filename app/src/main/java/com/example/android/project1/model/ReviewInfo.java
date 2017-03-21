package com.example.android.project1.model;

import java.util.List;

/**
 * Created by rgarc on 23/02/2017.
 */

public class ReviewInfo
{
    private String id;

    public String getId() { return this.id; }

    public void setId(String id) { this.id = id; }

    private String author;

    public String getAuthor() { return this.author; }

    public void setAuthor(String author) { this.author = author; }

    private String content;

    public String getContent() { return this.content; }

    public void setContent(String content) { this.content = content; }

    private String url;

    public String getUrl() { return this.url; }

    public void setUrl(String url) { this.url = url; }

    public boolean isVisible() {
        return true;
    }

    public static class ReviewResult {
        private List<ReviewInfo> results;

        public List<ReviewInfo> getResults() {
            return results;
        }
    }
}
