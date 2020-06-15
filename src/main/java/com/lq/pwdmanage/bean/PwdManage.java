package com.lq.pwdmanage.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 密码管理对象
 * @author LQ
 * @date 2020/6/10 10:17
 */
public class PwdManage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    private String id;

    /**
     * 网站url
     */
    private String websiteUrl;

    /**
     * 网站名称
     */
    private String websiteName;

    /**
     * 账号
     */
    private String account;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 密码
     */
    private String pwd;

    /**
     * QQ号
     */
    private String qq;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String mobileNumber;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 密保问题
     */
    private List<SecurityQuestion> securityQuestions = new ArrayList<>();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<SecurityQuestion> getSecurityQuestions() {
        return securityQuestions;
    }

    public void setSecurityQuestions(List<SecurityQuestion> securityQuestions) {
        this.securityQuestions = securityQuestions;
    }

    @Override
    public String toString() {
        return "PwdManage{" +
                "id='" + id + '\'' +
                ", websiteUrl='" + websiteUrl + '\'' +
                ", websiteName='" + websiteName + '\'' +
                ", account='" + account + '\'' +
                ", userName='" + userName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", pwd='" + pwd + '\'' +
                ", qq='" + qq + '\'' +
                ", email='" + email + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", remarks='" + remarks + '\'' +
                ", securityQuestions=" + securityQuestions +
                '}';
    }

}
