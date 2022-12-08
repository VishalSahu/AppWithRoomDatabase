package com.anr.appwithroomdatabase.roomdatabase;


import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.anr.appwithroomdatabase.roomdatabase.datamodel.UserModel;

@Dao
public interface EntityDao {
    @Query("select * from usermodel where email_Id=:email")
    LiveData<UserModel> getSingleRecord(String email);

    @Query("select first_name from usermodel where email_Id=:email")
    String checkEmailInDatabase(String email);

    @Query("select password from usermodel where email_Id=:email")
    String getPasswordFromDatabase(String email);

    @Query("UPDATE UserModel set 'password'=:newPassword WHERE email_Id=:email")
    void changePassword(String email, String newPassword);

    @Query("UPDATE UserModel set 'profile_pic'=:newBase64 WHERE email_Id=:email")
    void changeProfilePicture(String email, String newBase64);

    @Query("SELECT profile_pic FROM usermodel WHERE email_Id=:userEmail")
    String getProfilePicBase64(String userEmail);

    @Insert
    void addUser(UserModel userModel);
}
