package com.royalteck.progtobi.fintrakhr.LocalDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.royalteck.progtobi.fintrakhr.model.UserModel;
@Database(entities = {UserModel.class, }, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context,
                            AppDatabase.class, "fintrak_db").build();
        }
        return INSTANCE;
    }

    public abstract PatrecDAO mPatrecDAO();

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
