package com.example.kuouweather.bean;


public class Province {
    private String provinceName;
    private int provinceID;

    public Province(String provinceName, int provinceID) {
        this.provinceName = provinceName;
        this.provinceID = provinceID;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(int provinceID) {
        this.provinceID = provinceID;
    }
}
