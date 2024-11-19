package com.citronix.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "harvest_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HarvestDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "harvest_id", nullable = false)
    private Harvest harvest;

    @ManyToOne
    @JoinColumn(name = "tree_id", nullable = false)
    private Tree tree;

    @PositiveOrZero
    private Double quantity;

    public boolean validateTreeHarvest() {
        // A tree cannot be harvested more than once in the same season
        return harvest.getHarvestDetails().stream().noneMatch(detail -> detail.getTree().equals(tree));
    }
}