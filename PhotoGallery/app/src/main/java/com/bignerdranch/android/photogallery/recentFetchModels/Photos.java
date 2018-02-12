package com.bignerdranch.android.photogallery.recentFetchModels;

import java.util.List;

/**
 * Created by Kyeongil Han on 2018-02-06.
 */

public class Photos {
    private String page;
    private String pages;
    private String perpage;
    private String total;
    List<Photo> photo;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getPerpage() {
        return perpage;
    }

    public void setPerpage(String perpage) {
        this.perpage = perpage;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Photo> getPhoto() {
        return photo;
    }

    public void setPhoto(List<Photo> photo) {
        this.photo = photo;
    }
}
