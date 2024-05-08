package com.integration.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Employee {
    private int id;
    private String name;
    private int age;
    private String gender;
    private double salary;
}
