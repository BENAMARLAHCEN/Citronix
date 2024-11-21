package com.citronix.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleDTO {
    private Long id;
    @PastOrPresent
    private LocalDate saleDate;
    @Min(value = 1, message = "Unit price should be at least 1")
    private Double unitPrice;
    @Min(value = 1, message = "Quantity should be at least 1")
    private Double quantity;
    private String client;
    private Double revenue;
    private Long harvestId;
}