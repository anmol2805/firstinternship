package com.tophawks.vm.visualmerchandising.Training;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UploadPojo{

    public String name;
    public String url;
    public String modules;
    public String minutes;
    public String learners;
public String summary;
    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public UploadPojo() {
    }

    public UploadPojo(String name, String url, String modules, String minutes,String learners,String summary) {
        this.name = name;
        this.url= url;
        this.modules=modules;
        this.minutes=minutes;
        this.learners=learners;
        this.summary=summary;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
    public String getModules(){return modules;}
    public String getMinutes(){return minutes;}
    public String getLearners(){return learners;}
    public String getSummary(){return summary;}
}