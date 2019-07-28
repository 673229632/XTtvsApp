//******************************************************************************
// 版权所有(c) 2019，科大国创软件股份有限公司，保留所有权利。
//******************************************************************************

package com.gcsoft.xttvsapp.form;

import java.io.Serializable;

/**
 * 发送的数据的实体.
 * 
 * @author zhangrui.i
 * @since 2019年3月29日 下午4:28:02
 */
public class SocketSendInfoForm implements Serializable {

    private static final long serialVersionUID = -1043564333581733294L;

    // 一般请求地址
    private String typeUrl;

    // 血透系统中的请求
    private String typeXtUrl;

    // APP控制指令
    private String typeInstruction;

    // 文本
    private String typeText;

    // 显示时长(秒)
    private Integer showTimeLength;

    // 当前送发送的客户端ip
    private String clientIpAddress;

    // 是否播报语音
    private String isTextToSpeech;

    // 语音内容
    private TextToSpeechContent textToSpeechContent;
    /**
     * typeUrl的GET方法.
     *
     * @return typeUrl
     */
    public String getTypeUrl() {
        return typeUrl;
    }

    /**
     * typeUrl的SET方法.
     *
     * @param typeUrl typeUrl
     */
    public void setTypeUrl(String typeUrl) {
        this.typeUrl = typeUrl;
    }

    /**
     * typeXtUrl的GET方法.
     *
     * @return typeXtUrl
     */
    public String getTypeXtUrl() {
        return typeXtUrl;
    }

    /**
     * typeXtUrl的SET方法.
     *
     * @param typeXtUrl typeXtUrl
     */
    public void setTypeXtUrl(String typeXtUrl) {
        this.typeXtUrl = typeXtUrl;
    }

    /**
     * typeInstruction的GET方法.
     *
     * @return typeInstruction
     */
    public String getTypeInstruction() {
        return typeInstruction;
    }

    /**
     * typeInstruction的SET方法.
     *
     * @param typeInstruction typeInstruction
     */
    public void setTypeInstruction(String typeInstruction) {
        this.typeInstruction = typeInstruction;
    }

    /**
     * typeText的GET方法.
     *
     * @return typeText
     */
    public String getTypeText() {
        return typeText;
    }

    /**
     * typeText的SET方法.
     *
     * @param typeText typeText
     */
    public void setTypeText(String typeText) {
        this.typeText = typeText;
    }

    /**
     * showTimeLength的GET方法.
     *
     * @return showTimeLength
     */
    public Integer getShowTimeLength() {
        return showTimeLength;
    }

    /**
     * showTimeLength的SET方法.
     *
     * @param showTimeLength showTimeLength
     */
    public void setShowTimeLength(Integer showTimeLength) {
        this.showTimeLength = showTimeLength;
    }

    /**
     * clientIpAddress的GET方法.
     *
     * @return clientIpAddress
     */
    public String getClientIpAddress() {
        return clientIpAddress;
    }

    /**
     * clientIpAddress的SET方法.
     *
     * @param clientIpAddress clientIpAddress
     */
    public void setClientIpAddress(String clientIpAddress) {
        this.clientIpAddress = clientIpAddress;
    }

    public String getIsTextToSpeech() {
        return isTextToSpeech;
    }

    public void setIsTextToSpeech(String isTextToSpeech) {
        this.isTextToSpeech = isTextToSpeech;
    }

    public TextToSpeechContent getTextToSpeechContent() {
        return textToSpeechContent;
    }

    public void setTextToSpeechContent(TextToSpeechContent textToSpeechContent) {
        this.textToSpeechContent = textToSpeechContent;
    }
}
