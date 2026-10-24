package com.ptit.workhours;

import android.content.Context;
import android.content.SharedPreferences;

/** Stores the logged-in account in SharedPreferences. */
public class SessionManager {
    private final SharedPreferences p;

    public SessionManager(Context c) { p = c.getSharedPreferences("session", Context.MODE_PRIVATE); }

    public void login(int accountId, String username, String role, int employeeId) {
        p.edit().putBoolean("logged", true).putInt("accountId", accountId).putString("username", username)
                .putString("role", role).putInt("employeeId", employeeId).apply();
    }
    public boolean isLoggedIn() { return p.getBoolean("logged", false); }
    public String getRole() { return p.getString("role", ""); }
    public String getUsername() { return p.getString("username", ""); }
    public int getEmployeeId() { return p.getInt("employeeId", -1); }
    public void logout() { p.edit().clear().apply(); }
}
