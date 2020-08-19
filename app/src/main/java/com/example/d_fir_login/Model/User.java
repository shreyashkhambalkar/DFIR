package com.example.d_fir_login.Model;

public class User {

    private String Name;
    private String EmployeeId;
    private String PhoneNumber;
    private String Password;
    private String Designation;
    private String Age;

    public User(){

    }

    public User(String name, String employeeId, String phoneNumber, String password, String designation, String age) {
        Name = name;
        EmployeeId = employeeId;
        PhoneNumber = phoneNumber;
        Password = password;
        Designation = designation;
        Age = age;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(String employeeId) {
        EmployeeId = employeeId;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }
}