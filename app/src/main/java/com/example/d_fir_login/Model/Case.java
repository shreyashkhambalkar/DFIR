package com.example.d_fir_login.Model;

public class Case {

    private String CaseId;
    private String CaseCategory;
    private String OfficerName;
    private String TimeStamp;
    private String Age;

    public Case(){

    }

    public Case(String caseId, String caseCategory, String officerName, String timeStamp, String age) {
        CaseId = caseId;
        CaseCategory = caseCategory;
        OfficerName = officerName;
        TimeStamp = timeStamp;
        Age = age;
    }

    public String getCaseId() {
        return CaseId;
    }

    public void setCaseId(String caseId) {
        CaseId = caseId;
    }

    public String getCaseCategory() {
        return CaseCategory;
    }

    public void setCaseCategory(String caseCategory) {
        CaseCategory = caseCategory;
    }

    public String getOfficerName() {
        return OfficerName;
    }

    public void setOfficerName(String officerName) {
        OfficerName = officerName;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }
}