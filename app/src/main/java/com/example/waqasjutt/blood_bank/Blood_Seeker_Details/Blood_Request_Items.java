package com.example.waqasjutt.blood_bank.Blood_Seeker_Details;

import java.io.Serializable;

public class Blood_Request_Items implements Serializable {

    private String name;
    private String mobile;
    private String city;
    private String blood_group;
    private String hospital;
    private String blood_bags;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getBlood_bags() {
        return blood_bags;
    }

    public void setBlood_bags(String blood_bags) {
        this.blood_bags = blood_bags;
    }
}