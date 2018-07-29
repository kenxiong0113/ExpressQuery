package com.ken.expressquery.model;

import android.media.MediaPlayer;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;

import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

/**
 * 快递信息跟踪 实体类
 * 快递基本信息
 * @author by ken on 2017/9/23.
 */
@Entity
public class ExpressInfo {

    /**
     * code : OK
     * no : 3904231953322
     * type : YD
     * list : [{"content":"[湖北监利县公司]快件已被 本人 签收","time":"2018-05-11 10:10:12"},{"content":"[湖北监利县公司]进行派件扫描；派送业务员：谢模钊；联系电话：18071495395","time":"2018-05-11 09:58:32"},{"content":"[湖北武汉分拨中心]从站点发出，本次转运目的地：湖北监利县公司","time":"2018-05-10 22:37:37"},{"content":"[湖北武汉分拨中心]在分拨中心进行称重扫描","time":"2018-05-10 22:32:59"},{"content":"[湖北监利县公司]进行装车扫描，即将发往：湖北武汉分拨中心","time":"2018-05-10 18:13:26"},{"content":"[湖北监利县公司]进行下级地点扫描，将发往：湖北武汉分拨中心","time":"2018-05-10 17:39:58"},{"content":"[湖北监利县公司]进行揽件扫描","time":"2018-05-10 16:27:32"}]
     * state : 3
     * msg : 查询成功
     * name : 韵达快递
     * site : www.yundaex.com
     * phone : 95546
     * logo : http://img3.fegine.com/express/yd.jpg
     */
    @Id(autoincrement = true)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @Transient
    private String code;
    /** 唯一约束*/
    @Unique
    private String no;
    @Transient
    private String type;
    @Transient
    private String state;
    @Transient
    private String msg;
    private String name;
    @Transient
    private String site;
    @Transient
    private String phone;
    private String logo;

    @Convert( converter = ExpressListConvert.class,columnType = String.class)
    private List<LogisticsTrack> list = new ArrayList<LogisticsTrack>();


    @Generated(hash = 1882813502)
    public ExpressInfo() {
    }

    @Generated(hash = 2136022030)
    public ExpressInfo(Long id, String no, String name, String logo, List<LogisticsTrack> list) {
        this.id = id;
        this.no = no;
        this.name = name;
        this.logo = logo;
        this.list = list;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<LogisticsTrack> getList() {
        return list;
    }

    public void setList(List<LogisticsTrack> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public static class LogisticsTrack {
        /**
         * content : [湖北监利县公司]快件已被 本人 签收
         * time : 2018-05-11 10:10:12
         */

        private String content;
        private String time;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        @Override
        public String toString() {
            return super.toString();
        }

        public LogisticsTrack(String content,String time){
            this.content = content;
            this.time = time;
        }
    }
}

