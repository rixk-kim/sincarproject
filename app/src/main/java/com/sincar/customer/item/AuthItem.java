package com.sincar.customer.item;

public class AuthItem {
    public String AUTH_RESULT;
    public String CAUSE;
    public String AUTH_NUMBER;

    @Override
    public String toString() {
        return "AuthItem{" +
                "AUTH_RESULT='" + AUTH_RESULT + '\'' +
                ", CAUSE='" + CAUSE + '\'' +
                ", AUTH_NUMBER='" + AUTH_NUMBER + '\'' +
                '}';
    }
}
