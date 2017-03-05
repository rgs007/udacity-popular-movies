package com.example.android.project1.Model;

import java.util.List;

/**
 * Created by rgarc on 23/02/2017.
 */

public class TrailerInfo
{
    private String id;
    private String iso_639_1;
    private String iso_3166_1;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;

    public String getId() { return this.id; }

    public void setId(String id) { this.id = id; }

    public String getIso6391() { return this.iso_639_1; }

    public void setIso6391(String iso_639_1) { this.iso_639_1 = iso_639_1; }

    public String getIso31661() { return this.iso_3166_1; }

    public void setIso31661(String iso_3166_1) { this.iso_3166_1 = iso_3166_1; }

    public String getKey() { return this.key; }

    public void setKey(String key) { this.key = key; }

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    public String getSite() { return this.site; }

    public void setSite(String site) { this.site = site; }

    public int getSize() { return this.size; }

    public void setSize(int size) { this.size = size; }

    public String getType() { return this.type; }

    public void setType(String type) { this.type = type; }

    public static class TrailerResult {
        private List<TrailerInfo> results;

        public List<TrailerInfo> getResults() {
            return results;
        }
    }
}
