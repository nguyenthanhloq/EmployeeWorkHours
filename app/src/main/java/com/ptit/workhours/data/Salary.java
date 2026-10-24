package com.ptit.workhours.data;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Salary", indices = {@Index(value = {"employeeId", "month", "year"}, unique = true)})
public class Salary {
    @PrimaryKey(autoGenerate = true) public int id;
    public int employeeId;
    public int month;
    public int year;
    public double totalHours;
    public double overtimeHours;
    public double totalSalary;
    public String status;        // UNPAID or PAID
}
