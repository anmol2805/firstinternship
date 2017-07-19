package com.tophawks.vm.visualmerchandising.model;

public class UnplannedPojo
{
    private String storename;
    private String distance;
    private String storeid;
    private String mobile;
    private String email;
    private String channel;
    private String category;
    private String classification;
    private String city;
    private String address; 
    public UnplannedPojo(){};
    public UnplannedPojo(String storename,String distance,String storeid,String mobile,String email,String channel,String category
    ,String classification,String city, String address)
    {
        this.storename=storename;
        this.distance=distance;
        this.storeid=storeid;
        this.mobile=mobile;
        this.email=email;
        this.channel=channel;
        this.category=category;
        this.classification=classification;
        this.city=city;
        this.address=address;
    }
    public String getStorename(){return storename;}
    public String getDistance(){return distance;}
    public String getStoreid(){return storeid;}
    public String getMobile(){return mobile;}
    public String getEmail(){return email;}
    public String getChannel(){return channel;}
    public String getCategory(){return category;}
    public String getClassification(){return classification;}
    public String getCity(){return city;}
    public String getAddress(){return address;}
}
