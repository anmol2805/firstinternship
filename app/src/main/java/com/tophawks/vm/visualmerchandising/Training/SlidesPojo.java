package com.tophawks.vm.visualmerchandising.Training;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class SlidesPojo{

    public String heading;
    public String url;
    public String info;
    public String isimage;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public SlidesPojo() {
    }

    public SlidesPojo(String heading, String url,String info,String isimage) {
        this.heading = heading;
        this.url= url;
        this.info=info;
        this.isimage=isimage;
    }

    public String getHeading() {
        return heading;
    }
    public String getUrl() {
        return url;
    }
    public String getInfo(){return info;}
    public String getIsimage(){return isimage;}
}