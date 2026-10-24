package com.ptit.workhours.data;

import androidx.room.*;
import java.util.List;

@Dao
public interface WorkLogDao {
    @Insert long insert(WorkLog w);
    @Update void update(WorkLog w);
    @Query("SELECT * FROM WorkLog WHERE employeeId = :eid AND workDate = :date LIMIT 1")
    WorkLog findByEmployeeAndDate(int eid, String date);
    @Query("SELECT * FROM WorkLog WHERE employeeId = :eid ORDER BY workDate DESC") List<WorkLog> getByEmployee(int eid);
    @Query("SELECT * FROM WorkLog WHERE employeeId = :eid AND status = 'COMPLETED' AND workDate LIKE :prefix")
    List<WorkLog> getCompletedByMonth(int eid, String prefix);
    @Query("SELECT * FROM WorkLog WHERE checkOutTime IS NULL AND status = 'IN_PROGRESS' AND workDate < :today")
    List<WorkLog> getStale(String today);
    @Query("DELETE FROM WorkLog WHERE employeeId = :eid") void deleteByEmployee(int eid);
}
