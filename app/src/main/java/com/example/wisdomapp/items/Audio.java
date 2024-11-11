package com.example.wisdomapp.items;


public class Audio {

    private int id;
    private String title;
    private String path;
    private String timestamp;

    public Audio(int id, String title, String path, String timestamp) {
        this.id = id;
        this.title = title;
        this.path = path;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPath() { return path;
    }

    public String getTimestamp() {
        return timestamp;
    }
}




