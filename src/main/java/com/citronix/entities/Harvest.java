package com.citronix.entities;

import com.citronix.entities.enums.Season;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "harvests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Harvest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate harvestDate;

    @Enumerated(EnumType.STRING)
    private Season season;

    @PositiveOrZero
    private Double totalQuantity;

    @PositiveOrZero
    private Double remainingQuantity;

    @ManyToOne
    @JoinColumn(name = "field_id", nullable = false)
    private Field field;

    @OneToMany(mappedBy = "harvest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HarvestDetail> harvestDetails;

    public boolean validateHarvestSeason() {
        return season != null;
    }

    public void calculateTotalQuantity() {
        this.totalQuantity = harvestDetails.stream().mapToDouble(HarvestDetail::getQuantity).sum();
    }

    public void updateRemainingQuantity(double quantitySold) {
        this.remainingQuantity -= quantitySold;
    }
}