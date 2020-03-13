package com.sincar.customer.item;

import java.util.ArrayList;

public class LoginResult {
    public ArrayList<LoginItem> login = new ArrayList();
    public ArrayList<LoginDataItem> DATA = new ArrayList();
    public ArrayList<LoginAdvertiseItem> advertise = new ArrayList();

    @Override
    public String toString() {
        return "LoginResult{" +
                "login=" + login +
                ", DATA=" + DATA +
                ", advertise=" + advertise +
                '}';
    }
}
