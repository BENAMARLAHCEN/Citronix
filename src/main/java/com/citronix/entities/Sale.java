package com.citronix.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate saleDate;

    @Positive
    private Double unitPrice;

    @Positive
    private Double quantity;

    @NotBlank
    private String client;

    @ManyToOne
    @JoinColumn(name = "harvest_id", nullable = false)
    private Harvest harvest;

    public double calculateRevenue() {
        return unitPrice * quantity;
    }

    public boolean validateQuantity() {
        return quantity <= harvest.getRemainingQuantity();
    }
}