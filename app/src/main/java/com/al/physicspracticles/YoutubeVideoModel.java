package com.al.physicspracticles;

public class YoutubeVideoModel {
    public String name,url,type;
    //type=Youtube,Video

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public YoutubeVideoModel(String name, String url, String type) {
        this.name = name;
        this.url = url;
        this.type=type;
    }

    public YoutubeVideoModel() {
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
