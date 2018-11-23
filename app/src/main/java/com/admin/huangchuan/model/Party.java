package com.admin.huangchuan.model;

/**
 * Created by Administrator on 2018/2/27 0027.
 */
public class Party {
    private String id;// 主键
    private String detail;// 内容
    private String phone;// 联系人电话
    private String areaName;//支部名称
    private String status;// 状态，0未处理，1已处理
    private String name;// 联系人姓名
    private String resultDate;// 处理日期
    private String createDate;// 提交日期
    private String type;// 1约见党代表2社情民意

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResultDate() {
        return resultDate;
    }

    public void setResultDate(String resultDate) {
        this.resultDate = resultDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
