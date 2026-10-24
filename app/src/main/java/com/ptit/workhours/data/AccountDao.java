package com.ptit.workhours.data;

import androidx.room.*;

@Dao
public interface AccountDao {
    @Insert long insert(Account a);
    @Update void update(Account a);
    @Delete void delete(Account a);
    @Query("SELECT COUNT(*) FROM Account") int count();
    @Query("SELECT * FROM Account WHERE username = :u AND password = :p LIMIT 1") Account login(String u, String p);
    @Query("SELECT * FROM Account WHERE username = :u LIMIT 1") Account findByUsername(String u);
    @Query("SELECT * FROM Account WHERE id = :id LIMIT 1") Account getById(int id);
}
