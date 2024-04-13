package com.example.contactapp.Data;

import android.text.TextUtils;

public class CallDetails {
    private String name;
    private String number;
    private String time;
    private String sim;
    private String type;
    private long timeMills;
    private String key;

    public CallDetails(String name, String number, String time, String sim, String type, long timeMills) {
        this.name = name;
        this.number = number;
        this.time = time;
        this.sim = sim;
        this.type = type;
        this.timeMills = timeMills;
        updateKey();
    }
    private void updateKey() {
        this.key = TextUtils.isEmpty(name) ? number : name;
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

    public String getSim() {
        return sim;
    }

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }
}
