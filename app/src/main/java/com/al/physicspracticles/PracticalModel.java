package com.al.physicspracticles;

import java.util.HashMap;
import java.util.List;

public class PracticalModel {
    public String english_manual_url;
    public String video_url;
    public String calculation_url;
    public List<TitleModel> papers_urls;
    public int index;

    public List<TitleModel> getPapers_urls() {
        return papers_urls;
    }

    public void setPapers_urls(List<TitleModel> papers_urls) {
        this.papers_urls = papers_urls;
    }

    public PracticalModel(String english_manual_url, String video_url, int index, List<TitleModel> papers_urls) {
        this.english_manual_url = english_manual_url;
        this.video_url = video_url;
        //this.calculation_url = calculation_url;
        this.index = index;
        this.papers_urls=papers_urls;
    }

    public String getCalculation_url() {
        return calculation_url;
    }

    public void setCalculation_url(String calculation_url) {
        this.calculation_url = calculation_url;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }



    public String getEnglish_manual_url() {
        return english_manual_url;
    }

    public void setEnglish_manual_url(String english_manual_url) {
        this.english_manual_url = english_manual_url;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }



    public PracticalModel() {
    }
}
