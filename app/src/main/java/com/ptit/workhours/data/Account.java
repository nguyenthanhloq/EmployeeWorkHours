package com.ptit.workhours.data;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Account", indices = {@Index(value = "username", unique = true)})
public class Account {
    @PrimaryKey(autoGenerate = true) public int id;
    public String username;
    public String password; // SHA-256 hash
    public String role;     // ADMIN or EMPLOYEE
}
