package com.kiricle.parser.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class Vacancy {
    private String name;
    private String date;
    private String location;
    private String company;

    private String reference;
}
