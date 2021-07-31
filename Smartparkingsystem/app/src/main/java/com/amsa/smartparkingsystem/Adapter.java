package com.amsa.smartparkingsystem;


public class Adapter {
    private String Phone;
    private String Gari;
    private String Slot;
    private String Time;

    public Adapter(){

    }
    public Adapter(String phone, String gari, String slot, String time) {
        Phone=phone;
        Gari=gari;
        Slot=slot;
        Time=time;
    }
    public Adapter(String phone, String gari) {
        Phone=phone;
        Gari=gari;
    }

    public String getSlot() {
        return Slot;
    }

    public String getTime() {
        return Time;
    }

    public void setGari(String gari) {
        Gari = gari;
    }

    public void setSlot(String slot) {
        Slot = slot;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getGari() {
        return Gari;
    }


}

