package com.example.instashop;

public class User
{
    private String Name;
    private String Email;
    private String Phone;

    public User()
    {

    }

    public User(String name, String phone, String email)
    {
        Name = name;
        Email = email;
        Phone = phone;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
