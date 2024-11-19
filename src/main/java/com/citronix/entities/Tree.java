package com.citronix.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tree {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PastOrPresent
    private LocalDate plantingDate;

    @ManyToOne
    @JoinColumn(name = "field_id", nullable = false)
    private Field field;

    private Boolean isProductive;

    public int calculateAge() {
        return LocalDate.now().getYear() - plantingDate.getYear();
    }

    public double calculateProductivity() {
        int age = calculateAge();
        if (age < 3) {
            return 2.5;
        } else if (age <= 10) {
            return 12.0;
        } else if (age <= 20) {
            return 20.0;
        } else {
            return 0.0;
        }
    }

    public boolean isPlantingSeasonValid() {
        int month = plantingDate.getMonthValue();
        return month >= 3 && month <= 5;
    }
}