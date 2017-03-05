package com.example.android.project1.Model;

import java.util.ArrayList;

public class TrailersResponse
{
    private int id;

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    private ArrayList<TrailerInfo> results;

    public ArrayList<TrailerInfo> getResults() { return this.results; }

    public void setResults(ArrayList<TrailerInfo> results) { this.results = results; }
}
