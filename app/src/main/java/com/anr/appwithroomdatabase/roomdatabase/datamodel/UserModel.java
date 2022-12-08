package com.anr.appwithroomdatabase.roomdatabase.datamodel;

import androidx.annotation.NonNull;
import androidx.room.*;


@Entity
public class UserModel {
    @PrimaryKey(autoGenerate = true)
    private int uid;
    @ColumnInfo(name = "first_name")
    private String firstName;
    @ColumnInfo(name = "last_name")
    private String lastName;
    @ColumnInfo(name = "email_Id")
    private String emailID;
    @ColumnInfo(name = "contact")
    private String contactNumber;
    @ColumnInfo(name = "password")
    private String password;
    @ColumnInfo(name = "profile_pic")
    private String profilePic_base64;


    public UserModel(@NonNull String firstName, @NonNull String lastName, @NonNull String emailID, @NonNull String contactNumber, @NonNull String password, String profilePic_base64) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailID = emailID;
        this.contactNumber = contactNumber;
        this.password = password;
        this.profilePic_base64 = profilePic_base64;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePic_base64() {
        return profilePic_base64;
    }

    public void setProfilePic_base64(String profilePic_base64) {
        this.profilePic_base64 = profilePic_base64;
    }
}
