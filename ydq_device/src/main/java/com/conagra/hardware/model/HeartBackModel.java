package com.conagra.hardware.model;

import java.io.Serializable;

/**
 * Created by yedongqin on 16/9/22.
 */
public class HeartBackModel implements Serializable {


    /**
     * FHR1 : 0
     * TOCO : 0
     * AFM : 0
     * SIGN : 1
     * BATT : 3
     * isFHR1 : 0
     * isTOCO : 0
     * isAFM : 0
     */

    private String FHR1;
    private String TOCO;
    private String AFM;
    private String SIGN;
    private String BATT;
    private String isFHR1;
    private String isTOCO;
    private String isAFM;

    public String getFHR1() {
        return FHR1;
    }

    public void setFHR1(String FHR1) {
        this.FHR1 = FHR1;
    }

    public String getTOCO() {
        return TOCO;
    }

    public void setTOCO(String TOCO) {
        this.TOCO = TOCO;
    }

    public String getAFM() {
        return AFM;
    }

    public void setAFM(String AFM) {
        this.AFM = AFM;
    }

    public String getSIGN() {
        return SIGN;
    }

    public void setSIGN(String SIGN) {
        this.SIGN = SIGN;
    }

    public String getBATT() {
        return BATT;
    }

    public void setBATT(String BATT) {
        this.BATT = BATT;
    }

    public String getIsFHR1() {
        return isFHR1;
    }

    public void setIsFHR1(String isFHR1) {
        this.isFHR1 = isFHR1;
    }

    public String getIsTOCO() {
        return isTOCO;
    }

    public void setIsTOCO(String isTOCO) {
        this.isTOCO = isTOCO;
    }

    public String getIsAFM() {
        return isAFM;
    }

    public void setIsAFM(String isAFM) {
        this.isAFM = isAFM;
    }
}
