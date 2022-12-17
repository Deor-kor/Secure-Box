package com.example.cj;

public class Ob_Log {

    String log;
    String time;

    public Ob_Log() {
    }

    public Ob_Log(String log, String time) {
        this.log=log;
        this.time=time;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
}
