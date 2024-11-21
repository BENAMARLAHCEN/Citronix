package com.citronix.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HarvestDetailDTO {
    private Long id;
    private Long harvestId;
    private Long treeId;
    private Double quantity;
}