package com.ptit.workhours.data;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "WorkLog", indices = {@Index("employeeId"), @Index(value = {"employeeId", "workDate"}, unique = true)})
public class WorkLog {
    @PrimaryKey(autoGenerate = true) public int id;
    public int employeeId;
    public String workDate;      // yyyy-MM-dd
    public Long checkInTime;     // epoch millis
    public Long checkOutTime;    // epoch millis, null until check-out
    public double totalHours;
    public String status;        // IN_PROGRESS, COMPLETED, INCOMPLETE
}
