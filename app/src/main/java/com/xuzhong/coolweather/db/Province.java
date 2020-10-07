package com.xuzhong.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * 省 {"id":1,"name":"北京"}
 * 引入org.litepal.crud.DataSupport，会自动建立类相应的数据库，表
 * id是每个实体类中都应该有的字段，provinceName记录省的名字，provinceCode记录省的代号
 * ///////LitePalSupport类来替代DataSupport类
 */

public class Province extends DataSupport {
    private int id;
    private String provinceName;
    private int provinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}