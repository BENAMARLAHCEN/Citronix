package com.citronix.dto;

import com.citronix.utils.validation.ValidPlantingSeason;
import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreeDTO {

    private Long id;

    @PastOrPresent
    @ValidPlantingSeason
    private LocalDate plantingDate;

    private Long fieldId;

    private Boolean isProductive;
}