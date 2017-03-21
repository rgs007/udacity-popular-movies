package com.example.android.project1.model;

import java.util.ArrayList;

public class ReviewsReponse
{
    private int id;
    private int page;
    private ArrayList<ReviewInfo> results;
    private int total_pages;
    private int total_results;

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    public int getPage() { return this.page; }

    public void setPage(int page) { this.page = page; }

    public ArrayList<ReviewInfo> getResults() { return this.results; }

    public void setResults(ArrayList<ReviewInfo> results) { this.results = results; }

    public int getTotalPages() { return this.total_pages; }

    public void setTotalPages(int total_pages) { this.total_pages = total_pages; }

    public int getTotalResults() { return this.total_results; }

    public void setTotalResults(int total_results) { this.total_results = total_results; }
}
