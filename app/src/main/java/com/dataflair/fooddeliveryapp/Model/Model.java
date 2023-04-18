package com.dataflair.fooddeliveryapp.Model;

public class Model {

    String name, cityName, profilepic, pinCode, phoneNumber, address,userId;
    String hotelLocation, imageUrl, itemName, itemPrice;

    public Model() {
    }

    public Model(String name, String cityName, String profilepic, String pinCode, String phoneNumber, String address, String userId, String hotelLocation, String imageUrl, String itemName, String itemPrice) {
        this.name = name;
        this.cityName = cityName;
        this.profilepic = profilepic;
        this.pinCode = pinCode;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.userId = userId;
        this.hotelLocation = hotelLocation;
        this.imageUrl = imageUrl;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHotelLocation() {
        return hotelLocation;
    }

    public void setHotelLocation(String hotelLocation) {
        this.hotelLocation = hotelLocation;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }
}