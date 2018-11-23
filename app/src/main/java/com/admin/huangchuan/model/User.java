
package com.admin.huangchuan.model;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

/**
 * 用户登录信息
 */
public class User {
    @PrimaryKey(AssignType.BY_MYSELF)
    private String uuid; //登录令牌
    private String userName; //用户名
    private String realName;//真实姓名)
    private String roleName;//角色名
    private String userShow;//前端用户管理显示，0显示,1不显示
    private String newsShow;//新闻管理显示，0显示,1不显示
    private String volunteerShow;//志愿者管理显示，0显示,1不显示
    private String ddbgzsShow;//党代表工作室管理显示，0显示,1不显示
    private String countShow;//统计管理显示，0显示,1不显示

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getUserShow() {
        return userShow;
    }

    public void setUserShow(String userShow) {
        this.userShow = userShow;
    }

    public String getNewsShow() {
        return newsShow;
    }

    public void setNewsShow(String newsShow) {
        this.newsShow = newsShow;
    }

    public String getVolunteerShow() {
        return volunteerShow;
    }

    public void setVolunteerShow(String volunteerShow) {
        this.volunteerShow = volunteerShow;
    }

    public String getDdbgzsShow() {
        return ddbgzsShow;
    }

    public void setDdbgzsShow(String ddbgzsShow) {
        this.ddbgzsShow = ddbgzsShow;
    }

    public String getCountShow() {
        return countShow;
    }

    public void setCountShow(String countShow) {
        this.countShow = countShow;
    }
}
