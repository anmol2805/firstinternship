package com.tophawks.vm.visualmerchandising.model;

import java.io.Serializable;
import java.util.HashMap;


public class Product implements Serializable {

    private String productId;
    private String storeId;
    private String productName, imageUrl, storeName;
    private float originalPrice;

    private HashMap<String,Long> productQuantity;
    private String category, brandName;

    public Product() {
    }

    public Product(String productId,String storeId, String productName,
                   String storeName,String imageUrl, float originalPrice,
                   HashMap<String,Long> productQuantity, String category,
                   String brandName) {
        this.productId = productId;
        this.storeId = storeId;
        this.productName = productName;
        this.storeName = storeName;
        this.imageUrl = imageUrl;
        this.originalPrice = originalPrice;
        this.productQuantity = productQuantity;
        this.category = category;
        this.brandName = brandName;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(float originalPrice) {
        this.originalPrice = originalPrice;
    }

    public HashMap<String,Long> getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(HashMap<String,Long> productQuantity) {
        this.productQuantity = productQuantity;
    }


}
