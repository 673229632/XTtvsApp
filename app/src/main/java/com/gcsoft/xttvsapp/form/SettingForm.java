package com.gcsoft.xttvsapp.form;

import java.io.Serializable;

/**
 * 设置页面参数.
 *
 * @author zhangrui.i
 */
public class SettingForm implements Serializable {

    private String ip;

    private String xtname;

    private Integer port;

    private boolean powerup;

    private boolean fullscreen;

    private Integer height;

    private Integer width;

    private String bedurl;

    private float alpha;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getBedurl() {
        return bedurl;
    }

    public void setBedurl(String bedurl) {
        this.bedurl = bedurl;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getXtname() {
        return xtname;
    }

    public void setXtname(String xtname) {
        this.xtname = xtname;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public boolean isPowerup() {
        return powerup;
    }

    public void setPowerup(boolean powerup) {
        this.powerup = powerup;
    }
}
