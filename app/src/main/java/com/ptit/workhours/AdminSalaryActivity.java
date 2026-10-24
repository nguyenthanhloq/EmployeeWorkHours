package com.ptit.workhours;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ptit.workhours.data.AppDatabase;
import com.ptit.workhours.data.Employee;
import com.ptit.workhours.data.Salary;

import java.util.ArrayList;
import java.util.List;

public class AdminSalaryActivity extends AppCompatActivity {
    private AppDatabase db;
    private EditText etMonth, etYear;
    private ArrayAdapter<String> adapter;
    private List<Salary> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_salary);
        db = AppDatabase.get(this);
        etMonth = findViewById(R.id.etMonth);
        etYear = findViewById(R.id.etYear);
        etMonth.setText(String.valueOf(Utils.currentMonth()));
        etYear.setText(String.valueOf(Utils.currentYear()));

        ListView lv = findViewById(R.id.lvSalary);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        lv.setAdapter(adapter);
        lv.setOnItemClickListener((p, v, pos, id) -> {
            Salary s = data.get(pos);
            String next = "PAID".equals(s.status) ? "UNPAID" : "PAID";
            new AlertDialog.Builder(this).setTitle("Mark as " + next + "?")
                    .setPositiveButton("Yes", (d, w) -> { s.status = next; db.salaryDao().update(s); load(); })
                    .setNegativeButton("Cancel", null).show();
        });
        lv.setOnItemLongClickListener((p, v, pos, id) -> {
            Salary s = data.get(pos);
            new AlertDialog.Builder(this).setTitle("Delete this salary record?")
                    .setPositiveButton("Delete", (d, w) -> { db.salaryDao().delete(s); load(); })
                    .setNegativeButton("Cancel", null).show();
            return true;
        });
        findViewById(R.id.btnCalc).setOnClickListener(v -> calculateAll());
        load();
    }

    private int[] period() {
        try {
            int m = Integer.parseInt(etMonth.getText().toString().trim());
            int y = Integer.parseInt(etYear.getText().toString().trim());
            if (m < 1 || m > 12 || y < 2000) throw new NumberFormatException();
            return new int[]{m, y};
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Enter a valid month (1-12) and year", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void calculateAll() {
        int[] my = period();
        if (my == null) return;
        AttendanceService.closeStale(db);
        for (Employee e : db.employeeDao().getAll()) SalaryService.calculate(db, e.id, my[0], my[1]);
        load();
    }

    private void load() {
        int[] my = period();
        if (my == null) return;
        data = db.salaryDao().getByMonth(my[0], my[1]);
        List<String> rows = new ArrayList<>();
        for (Salary s : data) {
            Employee e = db.employeeDao().getById(s.employeeId);
            rows.add((e != null ? e.code + " - " + e.fullname : "Employee #" + s.employeeId)
                    + "\nHours: " + s.totalHours + " | Overtime: " + s.overtimeHours
                    + "\nSalary: " + Utils.money(s.totalSalary) + "  [" + s.status + "]");
        }
        adapter.clear();
        adapter.addAll(rows);
    }
}
