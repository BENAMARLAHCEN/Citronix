package com.citronix.repositories;

import com.citronix.entities.Harvest;
import com.citronix.entities.enums.Season;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface HarvestRepository extends JpaRepository<Harvest, Long> {
    boolean existsByFieldIdAndSeason(Long fieldId, Season season);
    boolean existsByFieldIdAndSeasonAndHarvestDate(Long fieldId, Season season, LocalDate harvestDate);
    boolean existsByHarvestDetailsTreeIdAndSeason(Long treeId, Season season);
}
