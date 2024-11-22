package UserDao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import model.User;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User user);
    @Query("SELECT * FROM users WHERE email= :email limit 1")
    User getUserFromEmail(String email);
}
