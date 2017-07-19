package com.tophawks.vm.visualmerchandising.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sanidhya on 18-Mar-17.
 */
public class Store implements Serializable {

    private String storeId;
    private String name;
    private String owner;
    private String shopAddress;
    private String channel;
    private String classification;
    private String category;
    private long capacity;
    private long contactNumber;
    private String storePic;
    private String stateAddress;
    private String cityAddress;
    private String emailId;
    private String latitude;
    private String longitude;
    private ArrayList<Product> storeProducts;

    public Store() {
    }

    public Store(String storeId, String name, String owner, String emailId, String shopAddress, String channel, String classification, String category, long capacity, long contactNumber, String storePic, String stateAddress, String cityAddress,String latitude,String longitude) {
        this.storeId = storeId;
        this.name = name;
        this.owner = owner;
        this.emailId = emailId;
        this.shopAddress = shopAddress;
        this.channel = channel;
        this.classification = classification;
        this.category = category;
        this.capacity = capacity;
        this.contactNumber = contactNumber;
        this.storePic = storePic;
        this.stateAddress = stateAddress;
        this.cityAddress = cityAddress;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public ArrayList<Product> getStoreProducts() {
        return storeProducts;
    }

    public void setStoreProducts(ArrayList<Product> storeProductIds) {
        this.storeProducts = storeProducts;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public long getCapacity() {
        return capacity;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    public long getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(long contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getStorePic() {
        return storePic;
    }

    public void setStorePic(String storePic) {
        this.storePic = storePic;
    }

    public String getStateAddress() {
        return stateAddress;
    }

    public void setStateAddress(String stateAddress) {
        this.stateAddress = stateAddress;
    }

    public String getCityAddress() {
        return cityAddress;
    }

    public void setCityAddress(String cityAddress) {
        this.cityAddress = cityAddress;
    }

}