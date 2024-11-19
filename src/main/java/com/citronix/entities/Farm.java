package com.citronix.entities;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Farm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String location;

    @Positive
    private Double totalArea;

    @PastOrPresent
    private LocalDate creationDate;

    @OneToMany(mappedBy = "farm", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Field> fields;

    public Double calculateRemainingArea() {
        double usedArea = fields.stream().mapToDouble(Field::getArea).sum();
        return totalArea - usedArea;
    }

    public boolean validateFieldsArea() {
        return fields.stream().mapToDouble(Field::getArea).sum() < totalArea;
    }
}
