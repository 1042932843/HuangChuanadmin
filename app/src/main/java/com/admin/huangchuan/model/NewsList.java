package com.admin.huangchuan.model;

/**
 * Created by Administrator on 2018/3/8 0008.
 */
public class NewsList {
    private  String id;
    private  String newsTypId;
    private  String newstitle;
    private  String areaName;
    private  String createDate;
    private  String author;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNewsTypId() {
        return newsTypId;
    }

    public void setNewsTypId(String newsTypId) {
        this.newsTypId = newsTypId;
    }

    public String getNewstitle() {
        return newstitle;
    }

    public void setNewstitle(String newstitle) {
        this.newstitle = newstitle;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
