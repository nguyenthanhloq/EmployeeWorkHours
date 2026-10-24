package com.ptit.workhours.data;

import androidx.room.*;
import java.util.List;

@Dao
public interface SalaryDao {
    @Insert long insert(Salary s);
    @Update void update(Salary s);
    @Delete void delete(Salary s);
    @Query("SELECT * FROM Salary WHERE employeeId = :eid AND month = :m AND year = :y LIMIT 1") Salary find(int eid, int m, int y);
    @Query("SELECT * FROM Salary WHERE employeeId = :eid ORDER BY year DESC, month DESC") List<Salary> getByEmployee(int eid);
    @Query("SELECT * FROM Salary WHERE month = :m AND year = :y ORDER BY employeeId") List<Salary> getByMonth(int m, int y);
    @Query("DELETE FROM Salary WHERE employeeId = :eid") void deleteByEmployee(int eid);
}
