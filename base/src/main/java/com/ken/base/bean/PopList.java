package com.ken.base.bean;

/**
 * 底部弹窗的数据列表实体类
 *
 * @author by ken on 2018//7/31
 *
 * */
public class PopList {
    String content;

    public PopList(String str) {
        this.content = str;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
