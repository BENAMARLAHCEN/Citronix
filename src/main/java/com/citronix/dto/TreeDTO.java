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

    @PastOrPresent
    @ValidPlantingSeason(message = "Planting season is from March to May")
    private LocalDate plantingDate;

    @NotNull
    @Positive
    private Long fieldId;

    private Boolean isProductive;
}