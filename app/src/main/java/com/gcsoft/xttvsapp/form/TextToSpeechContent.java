package com.gcsoft.xttvsapp.form;

import java.io.Serializable;

/**
 * @author zhangrui.i
 * @version V1.0
 * @Description: 语音播报内容.
 * @date
 */
public class TextToSpeechContent implements Serializable {

    // 模板
    private String template;
    // 显示时长
    private String showTime;

    // 患者姓名
    private String patientName;
    // 患者性别
     private String patientSex;
    // 时间带
    private String timtimeZone;
    // 床位位置
    private String bedPosition;

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getTimtimeZone() {
        return timtimeZone;
    }

    public void setTimtimeZone(String timtimeZone) {
        this.timtimeZone = timtimeZone;
    }

    public String getBedPosition() {
        return bedPosition;
    }

    public void setBedPosition(String bedPosition) {
        this.bedPosition = bedPosition;
    }

    public String getPatientSex() {
        return patientSex;
    }

    public void setPatientSex(String patientSex) {
        this.patientSex = patientSex;
    }
}
