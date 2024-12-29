package com.al.physicspracticles;

import android.graphics.Bitmap;
import android.net.Uri;

public class TitleModel {
    private String title;
    private Uri uri;
    private String root;
    private String Url;

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    private int res;

    public TitleModel( String id,String title) {
        this.title = title;
        this.id = id;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public TitleModel(String id, String title, String Url) {//for books
        this.title = title;
        this.id = id;
        this.Url=Url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public TitleModel() {
    }

    public TitleModel(String title, Uri uri,String root,String id) {
        this.title = title;
        this.uri = uri;
        this.root=root;
        this.id=id;
    }
    public TitleModel(String title, int res,String root,String id) {
        this.title = title;
        this.res = res;
        this.root=root;
        this.id=id;
    }
}
