package com.sincar.customer.network;

import java.io.Serializable;

/**
 * 2020.02.17 spirit
 * sincar
 * KEY & VALUE
 */
public class ParseValue implements Serializable {

    private String mParam;
    private String mValue;

    public ParseValue() {

    }

    public ParseValue(String param, String value) {
        this.mParam = param;
        this.mValue = value;
    }

    /**
     * @return the param
     */
    public String getParam() {
        return mParam;
    }

    /**
     * @param param the param to set
     */
    public void setParam(String param) {
        this.mParam = param;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return mValue;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.mValue = value;
    }

}
