package com.ptit.workhours;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.ptit.workhours.data.AppDatabase;
import com.ptit.workhours.data.Employee;

import java.util.ArrayList;
import java.util.List;

public class AdminMainActivity extends AppCompatActivity {
    private AppDatabase db;
    private EditText etSearch;
    private ArrayAdapter<String> adapter;
    private List<Employee> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionManager session = new SessionManager(this);
        if (!session.isLoggedIn() || !"ADMIN".equals(session.getRole())) { finish(); return; }
        setContentView(R.layout.activity_admin_main);
        db = AppDatabase.get(this);

        etSearch = findViewById(R.id.etSearch);
        ListView lv = findViewById(R.id.lvEmployees);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        lv.setAdapter(adapter);
        lv.setOnItemClickListener((p, v, pos, id) -> {
            Intent i = new Intent(this, EmployeeEditActivity.class);
            i.putExtra("id", data.get(pos).id);
            startActivity(i);
        });
        findViewById(R.id.btnAdd).setOnClickListener(v -> startActivity(new Intent(this, EmployeeEditActivity.class)));
        findViewById(R.id.btnSalary).setOnClickListener(v -> startActivity(new Intent(this, AdminSalaryActivity.class)));
        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            session.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            public void onTextChanged(CharSequence s, int a, int b, int c) { load(); }
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (db != null) load();
    }

    private void load() {
        String q = etSearch.getText().toString().trim();
        data = q.isEmpty() ? db.employeeDao().getAll() : db.employeeDao().search("%" + q + "%");
        List<String> rows = new ArrayList<>();
        for (Employee e : data) {
            rows.add(e.code + " - " + e.fullname + "\n" + e.email + " | " + e.phone + "\nRate: " + Utils.money(e.hourlyRate) + "/h");
        }
        adapter.clear();
        adapter.addAll(rows);
    }
}
