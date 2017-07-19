package com.tophawks.vm.visualmerchandising.Training;



public class TestsPojo {

    public String heading;
    public String testname;
    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public TestsPojo() {
    }

    public TestsPojo(String heading,String testname) {
        this.heading = heading;
        this.testname=testname;

    }

    public String getHeading() {
        return heading;
    }
    public String getTestname(){return testname;}

}