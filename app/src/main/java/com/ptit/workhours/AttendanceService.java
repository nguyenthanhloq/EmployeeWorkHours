package com.ptit.workhours;

import com.ptit.workhours.data.AppDatabase;
import com.ptit.workhours.data.WorkLog;

import java.util.List;

/** Check-in / check-out with validation. Each method returns an error message, or null on success. */
public class AttendanceService {

    /** Past days with a check-in but no check-out become INCOMPLETE (excluded from salary). */
    public static void closeStale(AppDatabase db) {
        List<WorkLog> stale = db.workLogDao().getStale(Utils.today());
        for (WorkLog w : stale) {
            w.status = "INCOMPLETE";
            db.workLogDao().update(w);
        }
    }

    public static String checkIn(AppDatabase db, int employeeId) {
        closeStale(db);
        WorkLog w = db.workLogDao().findByEmployeeAndDate(employeeId, Utils.today());
        if (w != null) {
            return w.checkOutTime == null ? "You have already checked in today."
                    : "You have already completed attendance for today.";
        }
        w = new WorkLog();
        w.employeeId = employeeId;
        w.workDate = Utils.today();
        w.checkInTime = System.currentTimeMillis();
        w.totalHours = 0;
        w.status = "IN_PROGRESS";
        db.workLogDao().insert(w);
        return null;
    }

    public static String checkOut(AppDatabase db, int employeeId) {
        WorkLog w = db.workLogDao().findByEmployeeAndDate(employeeId, Utils.today());
        if (w == null || w.checkInTime == null) return "You must check in before checking out.";
        if (w.checkOutTime != null) return "You have already checked out today.";
        long now = System.currentTimeMillis();
        if (now <= w.checkInTime) return "Invalid check-out time (must be after check-in).";
        w.checkOutTime = now;
        w.totalHours = Utils.round2((now - w.checkInTime) / 3600000.0);
        w.status = "COMPLETED";
        db.workLogDao().update(w);
        SalaryService.calculate(db, employeeId, Utils.currentMonth(), Utils.currentYear());
        return null;
    }
}
