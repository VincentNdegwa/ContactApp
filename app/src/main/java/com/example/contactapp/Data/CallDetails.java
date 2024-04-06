package com.example.contactapp.Data;

public class CallDetails {
    private String name;
    private String number;
    private String time;
    private int sim;
    private String type;
    private long timeMills;

    public CallDetails(String name, String number, String time, int sim, String type, long timeMills) {
        this.name = name;
        this.number = number;
        this.time = time;
        this.sim = sim;
        this.type = type;
        this.timeMills = timeMills;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getTime() {
        return time;
    }

    public long getTimeMills() {
        return timeMills;
    }

    public int getSim() {
        return sim;
    }

    public String getType() {
        return type;
    }
}
