package ru.velialcult.salary.storage;

import ru.velialcult.salary.salary.Salary;

import java.util.ArrayList;
import java.util.List;

public class SalaryStorage {

    private final List<Salary> salaryList;

    public SalaryStorage() {
        this.salaryList = new ArrayList<>();
    }

    public List<Salary> getSalaryList() {
        return salaryList;
    }
}
