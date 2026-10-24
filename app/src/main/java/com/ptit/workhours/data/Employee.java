package com.ptit.workhours.data;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Employee", indices = {@Index(value = "code", unique = true)})
public class Employee {
    @PrimaryKey(autoGenerate = true) public int id;
    public String code;
    public String fullname;
    public String email;
    public String phone;
    public double hourlyRate;
    public int accountId;
}
