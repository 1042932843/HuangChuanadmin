package com.admin.huangchuan.model;

/**
 * Created by Administrator on 2018/3/6 0006.
 */
public class UserDetails {
    private  String id;//主键id
    private  String joinPartyDate;//入党日期
    private  String position;//职位
    private  String picture;//头像图片
    private  String sex;//1男2女
    private  String phone;//电话
    private  String areaName;//所在党支部
    private  String realName;//姓名
    private  String idcard;//身份证号码
    private  String dysj;//第一书记(1否2是)
    private  String areaId;//所在党支部id


    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJoinPartyDate() {
        return joinPartyDate;
    }

    public void setJoinPartyDate(String joinPartyDate) {
        this.joinPartyDate = joinPartyDate;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getDysj() {
        return dysj;
    }

    public void setDysj(String dysj) {
        this.dysj = dysj;
    }
}
