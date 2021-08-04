package com.example.kuouweather.bean;


import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

public class Province extends LitePalSupport {
    /*增加Gson转换注解*/
    @SerializedName("name")
    private String provinceName;
    @SerializedName("id")
    private String provinceID;


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
