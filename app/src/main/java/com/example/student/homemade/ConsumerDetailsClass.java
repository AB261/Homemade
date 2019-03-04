package com.example.student.homemade;

public class ConsumerDetailsClass {
    String username, password, address, contactNo, wallet, email,typeOfUser;

    public ConsumerDetailsClass() {
    }

    public ConsumerDetailsClass(String username, String password, String address, String contactNo, String wallet, String email, String typeOfUser) {
        this.username = username;
        this.password = password;
        this.address = address;
        this.contactNo = contactNo;
        this.wallet = wallet;
        this.email = email;
        this.typeOfUser = typeOfUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTypeOfUser() {
        return typeOfUser;
    }

    public void setTypeOfUser(String typeOfUser) {
        this.typeOfUser = typeOfUser;
    }
}
