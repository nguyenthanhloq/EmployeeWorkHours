package com.ptit.workhours.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Account.class, Employee.class, WorkLog.class, Salary.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AccountDao accountDao();
    public abstract EmployeeDao employeeDao();
    public abstract WorkLogDao workLogDao();
    public abstract SalaryDao salaryDao();

    private static AppDatabase instance;

    public static synchronized AppDatabase get(Context c) {
        if (instance == null) {
            instance = Room.databaseBuilder(c.getApplicationContext(), AppDatabase.class, "workhours.db")
                    .allowMainThreadQueries() // fine for a small lab project
                    .build();
        }
        return instance;
    }
}
