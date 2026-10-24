package com.ptit.workhours;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ptit.workhours.data.Account;
import com.ptit.workhours.data.AppDatabase;
import com.ptit.workhours.data.Employee;

public class LoginActivity extends AppCompatActivity {
    private AppDatabase db;
    private SessionManager session;
    private EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = AppDatabase.get(this);
        session = new SessionManager(this);
        seedIfEmpty();
        if (session.isLoggedIn()) { route(session.getRole()); return; }

        setContentView(R.layout.activity_login);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        ((Button) findViewById(R.id.btnLogin)).setOnClickListener(v -> doLogin());
    }

    private void seedIfEmpty() {
        if (db.accountDao().count() > 0) return;
        Account admin = new Account();
        admin.username = "admin"; admin.password = Utils.hash("admin123"); admin.role = "ADMIN";
        db.accountDao().insert(admin);

        Account acc = new Account();
        acc.username = "emp1"; acc.password = Utils.hash("emp123"); acc.role = "EMPLOYEE";
        int accId = (int) db.accountDao().insert(acc);
        Employee e = new Employee();
        e.code = "E001"; e.fullname = "Nguyen Van A"; e.email = "a@example.com";
        e.phone = "0900000001"; e.hourlyRate = 50000; e.accountId = accId;
        db.employeeDao().insert(e);
    }

    private void doLogin() {
        String u = etUsername.getText().toString().trim();
        String p = etPassword.getText().toString();
        if (u.isEmpty() || p.isEmpty()) {
            Toast.makeText(this, "Enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }
        Account a = db.accountDao().login(u, Utils.hash(p));
        if (a == null) {
            Toast.makeText(this, "Wrong username or password", Toast.LENGTH_SHORT).show();
            return;
        }
        int empId = -1;
        if ("EMPLOYEE".equals(a.role)) {
            Employee e = db.employeeDao().getByAccountId(a.id);
            if (e == null) {
                Toast.makeText(this, "No employee profile for this account", Toast.LENGTH_SHORT).show();
                return;
            }
            empId = e.id;
        }
        session.login(a.id, a.username, a.role, empId);
        route(a.role);
    }

    private void route(String role) {
        startActivity(new Intent(this, "ADMIN".equals(role) ? AdminMainActivity.class : EmployeeMainActivity.class));
        finish();
    }
}
