package com.anr.appwithroomdatabase.roomdatabase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.anr.appwithroomdatabase.roomdatabase.datamodel.UserModel;

@Database(entities = UserModel.class, exportSchema = false, version = 3)
public abstract class DatabaseHelper extends RoomDatabase {
    private static final String DB_NAME = "userDb";
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getDB(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, DatabaseHelper.class, DB_NAME).addMigrations(new Migration(2, 3) {
                @Override
                public void migrate(@NonNull SupportSQLiteDatabase database) {
                    database.execSQL("ALTER TABLE 'Usermodel' ADD COLUMN 'country' TEXT");
                }
            }).allowMainThreadQueries().build();
        }
        return instance;
    }

    public abstract EntityDao entityDao();
}