package com.citronix.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FieldDTO {

    private Long id;

    @Positive
    @DecimalMin(value = "0.1", message = "Minimum area should be at least 0.1 hectares")
    private Double area;

    private Long farmId;

    private List<TreeDTO> trees;
}