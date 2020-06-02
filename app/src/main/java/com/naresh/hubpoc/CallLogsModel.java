package com.naresh.hubpoc;

class CallLogsModel {
    private String callerID;
    private String callerName;
    private String callerNumber;
    private String userNumber;
    private String countryCode;
    private long callDateAndTime;
    private long callDuration;
    private int callType;

    public int getCallType() {
        return callType;
    }

    public void setCallType(int callType) {
        this.callType = callType;
    }

    public String getCallerID() {
        return callerID;
    }

    public void setCallerID(String callerID) {
        this.callerID = callerID;
    }

    public String getCallerNumber() {
        return callerNumber;
    }

    public void setCallerNumber(String callerNumber) {
        this.callerNumber = callerNumber;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public long getCallDateAndTime() {
        return callDateAndTime;
    }

    public void setCallDateAndTime(long callDateAndTime) {
        this.callDateAndTime = callDateAndTime;
    }

    public long getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(long callDuration) {
        this.callDuration = callDuration;
    }

    public String getCallerName() {
        return callerName;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
