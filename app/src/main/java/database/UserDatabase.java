package database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import UserDao.UserDao;
import model.User;

@Database(entities = {User.class},version = 1,exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    private  static UserDatabase INSTANCE;
    public static UserDatabase getInstance(Context context){
        if(INSTANCE==null){
            INSTANCE= Room.databaseBuilder(context.getApplicationContext(),UserDatabase.class,"UserDB").build();
        }
        return INSTANCE;
    }
}
