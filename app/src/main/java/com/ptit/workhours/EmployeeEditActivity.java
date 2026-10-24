package com.ptit.workhours;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ptit.workhours.data.Account;
import com.ptit.workhours.data.AppDatabase;
import com.ptit.workhours.data.Employee;

public class EmployeeEditActivity extends AppCompatActivity {
    private AppDatabase db;
    private EditText etCode, etFullName, etEmail, etPhone, etRate, etUsername, etPassword;
    private Employee editing;   // null when adding
    private Account editingAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_edit);
        db = AppDatabase.get(this);
        etCode = findViewById(R.id.etCode);
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etRate = findViewById(R.id.etRate);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        int id = getIntent().getIntExtra("id", -1);
        if (id != -1) {
            editing = db.employeeDao().getById(id);
            editingAcc = db.accountDao().getById(editing.accountId);
            etCode.setText(editing.code);
            etFullName.setText(editing.fullname);
            etEmail.setText(editing.email);
            etPhone.setText(editing.phone);
            etRate.setText(String.valueOf(editing.hourlyRate));
            etUsername.setText(editingAcc.username);
            etUsername.setEnabled(false);
            etPassword.setHint("New password (leave blank to keep)");
            findViewById(R.id.btnDelete).setVisibility(View.VISIBLE);
        }
        findViewById(R.id.btnSave).setOnClickListener(v -> save());
        findViewById(R.id.btnDelete).setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Delete employee?")
                .setMessage("This also deletes their account, attendance and salary records.")
                .setPositiveButton("Delete", (d, w) -> delete())
                .setNegativeButton("Cancel", null).show());
    }

    private void save() {
        String code = etCode.getText().toString().trim();
        String name = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String user = etUsername.getText().toString().trim();
        String pass = etPassword.getText().toString();
        double rate;
        try { rate = Double.parseDouble(etRate.getText().toString().trim()); }
        catch (NumberFormatException ex) { toast("Hourly rate must be a number"); return; }

        if (code.isEmpty() || name.isEmpty() || user.isEmpty()) { toast("Code, full name and username are required"); return; }
        if (rate <= 0) { toast("Hourly rate must be greater than 0"); return; }
        if (!email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) { toast("Invalid email"); return; }
        if (!phone.isEmpty() && !phone.matches("\\d{9,11}")) { toast("Phone must be 9-11 digits"); return; }

        Employee sameCode = db.employeeDao().getByCode(code);
        if (sameCode != null && (editing == null || sameCode.id != editing.id)) { toast("Employee code already exists"); return; }

        if (editing == null) {
            if (pass.length() < 6) { toast("Password must be at least 6 characters"); return; }
            if (db.accountDao().findByUsername(user) != null) { toast("Username already exists"); return; }
            Account a = new Account();
            a.username = user; a.password = Utils.hash(pass); a.role = "EMPLOYEE";
            int accId = (int) db.accountDao().insert(a);
            Employee e = new Employee();
            fill(e, code, name, email, phone, rate);
            e.accountId = accId;
            db.employeeDao().insert(e);
        } else {
            if (!pass.isEmpty()) {
                if (pass.length() < 6) { toast("Password must be at least 6 characters"); return; }
                editingAcc.password = Utils.hash(pass);
                db.accountDao().update(editingAcc);
            }
            fill(editing, code, name, email, phone, rate);
            db.employeeDao().update(editing);
        }
        toast("Saved");
        finish();
    }

    private void fill(Employee e, String code, String name, String email, String phone, double rate) {
        e.code = code; e.fullname = name; e.email = email; e.phone = phone; e.hourlyRate = rate;
    }

    private void delete() {
        db.workLogDao().deleteByEmployee(editing.id);
        db.salaryDao().deleteByEmployee(editing.id);
        db.employeeDao().delete(editing);
        db.accountDao().delete(editingAcc);
        toast("Deleted");
        finish();
    }

    private void toast(String m) { Toast.makeText(this, m, Toast.LENGTH_SHORT).show(); }
}
