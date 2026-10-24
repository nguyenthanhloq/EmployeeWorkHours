package com.ptit.workhours.data;

import androidx.room.*;
import java.util.List;

@Dao
public interface EmployeeDao {
    @Insert long insert(Employee e);
    @Update void update(Employee e);
    @Delete void delete(Employee e);
    @Query("SELECT * FROM Employee ORDER BY fullname") List<Employee> getAll();
    @Query("SELECT * FROM Employee WHERE fullname LIKE :q OR code LIKE :q OR phone LIKE :q OR email LIKE :q ORDER BY fullname")
    List<Employee> search(String q);
    @Query("SELECT * FROM Employee WHERE id = :id LIMIT 1") Employee getById(int id);
    @Query("SELECT * FROM Employee WHERE accountId = :aid LIMIT 1") Employee getByAccountId(int aid);
    @Query("SELECT * FROM Employee WHERE code = :code LIMIT 1") Employee getByCode(String code);
}
