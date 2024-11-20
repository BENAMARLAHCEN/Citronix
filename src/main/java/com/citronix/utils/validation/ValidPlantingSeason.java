package com.citronix.utils.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PlantingSeasonValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPlantingSeason {
    String message() default "Planting date is not within the planting season";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}