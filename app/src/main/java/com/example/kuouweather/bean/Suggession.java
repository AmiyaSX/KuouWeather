package com.example.kuouweather.bean;

import org.litepal.crud.LitePalSupport;

public class Suggession extends LitePalSupport {
    private String comf;
    private String sport;
    private String cw;

    public Suggession(String comf, String sport, String cw) {
        this.comf = comf;
        this.sport = sport;
        this.cw = cw;
    }

    public String getComf() {
        return comf;
    }

    public void setComf(String comf) {
        this.comf = comf;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getCw() {
        return cw;
    }

    public void setCw(String cw) {
        this.cw = cw;
    }
}
