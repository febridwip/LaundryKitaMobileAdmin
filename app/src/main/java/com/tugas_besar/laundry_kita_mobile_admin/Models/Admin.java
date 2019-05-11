package com.tugas_besar.laundry_kita_mobile_admin.Models;

public class Admin {
    public String Nama, Email, Telepon, Password;

    public Admin() {

    }

    public Admin(String userName, String userEmail, String userPhone, String userPassword) {
        Nama        = userName;
        Email       = userEmail;
        Telepon     = userPhone;
        Password    = userPassword;
    }
}
