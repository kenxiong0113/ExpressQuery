package com.ken.expressquery.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

/**
 * @author by ken on 2018/5/14
 * GreenDao3实体套实体保存
 * 解决greendao不能匹配JsonArray数据类型
 */
public class ExpressListConvert implements PropertyConverter<List<ExpressInfo.LogisticsTrack>, String> {

    @Override
    public List<ExpressInfo.LogisticsTrack> convertToEntityProperty(String databaseValue) {
        Gson gson = new Gson();
        return gson.fromJson(databaseValue, new TypeToken<List<ExpressInfo.LogisticsTrack>>() {
        }.getType());
    }

    @Override
    public String convertToDatabaseValue(List<ExpressInfo.LogisticsTrack> entityProperty) {
        Gson gson = new Gson();
        return gson.toJson(entityProperty, new TypeToken<List<ExpressInfo.LogisticsTrack>>() {
        }.getType());
    }
}
