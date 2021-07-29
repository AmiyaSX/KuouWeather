package com.example.kuouweather.bean;


import org.litepal.crud.LitePalSupport;

public class Province extends LitePalSupport {
    private String provinceName;
    private String provinceID;

    public Province(String provinceName, String provinceID) {
        this.provinceName = provinceName;
        this.provinceID = provinceID;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

}
