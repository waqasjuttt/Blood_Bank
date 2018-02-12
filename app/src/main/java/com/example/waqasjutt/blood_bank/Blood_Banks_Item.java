package com.example.waqasjutt.blood_bank;

public class Blood_Banks_Item {

    String name, address, contact, contactImage, location;

    public Blood_Banks_Item(String name, String address, String contact, String contactImage, String location) {
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.contactImage = contactImage;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactImage() {
        return contactImage;
    }

    public void setContactImage(String contactImage) {
        this.contactImage = contactImage;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
