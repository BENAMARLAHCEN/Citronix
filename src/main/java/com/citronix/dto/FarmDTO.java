package com.citronix.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FarmDTO {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String location;

    @Positive
    private Double totalArea;

    @PastOrPresent
    private LocalDate creationDate;

    private List<FieldDTO> fields;
}