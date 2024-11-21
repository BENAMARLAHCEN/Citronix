package com.citronix.dto;

import lombok.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HarvestDTO {
    private Long id;

    @PastOrPresent
    private LocalDate harvestDate;

    @Pattern(regexp = "WINTER|SPRING|SUMMER|AUTUMN", message = "Season must be one of SPRING, SUMMER, FALL, WINTER")
    private String season;

    private Double totalQuantity;
    private Double remainingQuantity;


    private Long fieldId;

    private List<HarvestDetailDTO> harvestDetails;
}