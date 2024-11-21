package com.citronix.viewmodel;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class FieldVM {
    private Double area;

    private FarmVM farm;

    private List<TreeVM> trees;
}
