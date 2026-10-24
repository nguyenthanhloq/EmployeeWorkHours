package com.ptit.workhours;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.ptit.workhours.data.AppDatabase;
import com.ptit.workhours.data.Salary;

import java.util.ArrayList;
import java.util.List;

public class EmployeeSalaryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_salary);
        AppDatabase db = AppDatabase.get(this);
        int empId = new SessionManager(this).getEmployeeId();

        AttendanceService.closeStale(db);
        SalaryService.calculate(db, empId, Utils.currentMonth(), Utils.currentYear());

        List<String> rows = new ArrayList<>();
        for (Salary s : db.salaryDao().getByEmployee(empId)) {
            rows.add(String.format("%02d/%d  [%s]\nHours: %s | Overtime: %s\nSalary: %s",
                    s.month, s.year, s.status, s.totalHours, s.overtimeHours, Utils.money(s.totalSalary)));
        }
        ListView lv = findViewById(R.id.lvSalary);
        lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rows));
    }
}
