package com.ptit.workhours;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ptit.workhours.data.AppDatabase;
import com.ptit.workhours.data.Employee;
import com.ptit.workhours.data.Salary;
import com.ptit.workhours.data.WorkLog;

import java.util.ArrayList;
import java.util.List;

public class EmployeeMainActivity extends AppCompatActivity {
    private AppDatabase db;
    private SessionManager session;
    private int employeeId;
    private TextView tvStatus, tvSummary;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(this);
        if (!session.isLoggedIn() || !"EMPLOYEE".equals(session.getRole())) { finish(); return; }
        setContentView(R.layout.activity_employee_main);
        db = AppDatabase.get(this);
        employeeId = session.getEmployeeId();

        Employee me = db.employeeDao().getById(employeeId);
        ((TextView) findViewById(R.id.tvWelcome)).setText("Hello, " + (me != null ? me.fullname : session.getUsername()));
        tvStatus = findViewById(R.id.tvStatus);
        tvSummary = findViewById(R.id.tvSummary);
        ListView lv = findViewById(R.id.lvHistory);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        lv.setAdapter(adapter);

        findViewById(R.id.btnCheckIn).setOnClickListener(v -> {
            String err = AttendanceService.checkIn(db, employeeId);
            Toast.makeText(this, err == null ? "Checked in" : err, Toast.LENGTH_SHORT).show();
            refresh();
        });
        findViewById(R.id.btnCheckOut).setOnClickListener(v -> {
            String err = AttendanceService.checkOut(db, employeeId);
            Toast.makeText(this, err == null ? "Checked out" : err, Toast.LENGTH_SHORT).show();
            refresh();
        });
        findViewById(R.id.btnSalary).setOnClickListener(v -> startActivity(new Intent(this, EmployeeSalaryActivity.class)));
        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            session.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (db != null) refresh();
    }

    private void refresh() {
        AttendanceService.closeStale(db);
        WorkLog today = db.workLogDao().findByEmployeeAndDate(employeeId, Utils.today());
        if (today == null) tvStatus.setText("Today (" + Utils.today() + "): not checked in");
        else tvStatus.setText("Today (" + Utils.today() + "): in " + Utils.fmtTime(today.checkInTime)
                + ", out " + Utils.fmtTime(today.checkOutTime));

        Salary s = SalaryService.calculate(db, employeeId, Utils.currentMonth(), Utils.currentYear());
        tvSummary.setText(s == null ? "" : "This month: " + s.totalHours + " h (overtime " + s.overtimeHours
                + " h)\nSalary so far: " + Utils.money(s.totalSalary));

        List<String> rows = new ArrayList<>();
        for (WorkLog w : db.workLogDao().getByEmployee(employeeId)) {
            rows.add(w.workDate + "  [" + w.status + "]\nIn " + Utils.fmtTime(w.checkInTime)
                    + "  Out " + Utils.fmtTime(w.checkOutTime) + "  Hours: " + w.totalHours);
        }
        adapter.clear();
        adapter.addAll(rows);
    }
}
