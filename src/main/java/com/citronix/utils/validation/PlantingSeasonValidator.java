package com.citronix.utils.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class PlantingSeasonValidator implements ConstraintValidator<ValidPlantingSeason, LocalDate> {

    @Override
    public boolean isValid(LocalDate plantingDate, ConstraintValidatorContext context) {
        if (plantingDate == null) {
            return true; // null values are valid, use @NotNull for null checks
        }
        int month = plantingDate.getMonthValue();
        return month >= 3 && month <= 5;
    }
}