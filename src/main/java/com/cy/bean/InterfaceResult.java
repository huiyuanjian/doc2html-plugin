package com.cy.bean;

import java.io.Serializable;

/**
 * 返回页面的结果实体
 */
public class InterfaceResult implements Serializable {

    private boolean success;

    private String message;

    private String html;

    public InterfaceResult(boolean success, String message, String html) {
        this.success = success;
        this.message = message;
        this.html = html;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
