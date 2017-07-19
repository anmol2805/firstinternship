package com.tophawks.vm.visualmerchandising.Training;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class CardPojo{

    public String name;
    public String url;
    public String modules;
    public String minutes;
    public String iscompleted;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public CardPojo() {
    }

    public CardPojo(String name, String url, String modules, String minutes, String iscompleted) {
        this.name = name;
        this.url= url;
        this.modules=modules;
        this.minutes=minutes;
        this.iscompleted=iscompleted;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
    public String getModules(){return modules;}
    public String getMinutes(){return minutes;}
    public String getIscompleted(){return iscompleted;}
    public void setIscompleted(String iscompleted){this.iscompleted=iscompleted;}
}