package com.ptit.workhours;

import com.ptit.workhours.data.AppDatabase;
import com.ptit.workhours.data.Employee;
import com.ptit.workhours.data.Salary;
import com.ptit.workhours.data.WorkLog;

import java.util.List;

public class SalaryService {

    /**
     * totalHours    = all completed hours in the month
     * overtimeHours = sum of (daily hours - 8) for days over 8h
     * totalSalary   = regular * rate + overtime * rate * 1.5
     * PAID records are never changed.
     */
    public static Salary calculate(AppDatabase db, int employeeId, int month, int year) {
        Employee e = db.employeeDao().getById(employeeId);
        if (e == null) return null;
        Salary old = db.salaryDao().find(employeeId, month, year);
        if (old != null && "PAID".equals(old.status)) return old;

        List<WorkLog> logs = db.workLogDao().getCompletedByMonth(employeeId, Utils.monthPrefix(month, year));
        double total = 0, ot = 0;
        for (WorkLog w : logs) {
            total += w.totalHours;
            ot += Math.max(0, w.totalHours - Utils.STANDARD_HOURS_PER_DAY);
        }
        total = Utils.round2(total);
        ot = Utils.round2(ot);
        double regular = total - ot;

        Salary s = old != null ? old : new Salary();
        s.employeeId = employeeId;
        s.month = month;
        s.year = year;
        s.totalHours = total;
        s.overtimeHours = ot;
        s.totalSalary = Utils.round2(regular * e.hourlyRate + ot * e.hourlyRate * Utils.OVERTIME_RATE);
        if (old == null) {
            s.status = "UNPAID";
            s.id = (int) db.salaryDao().insert(s);
        } else {
            db.salaryDao().update(s);
        }
        return s;
    }
}
