package com.cy.service;

/**
 * 图片标签枚举类
 */
public enum ImgTagName {

    /**
     * 普通图片
     */
    IMG("img", "支持jpeg"),

    /**
     * 高清图片
     */
    IMAGEDATA("imagedata", "支持PNG");

    private String key;

    private String value;

    ImgTagName(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
