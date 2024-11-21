package com.citronix.repositories;

import com.citronix.entities.Harvest;
import com.citronix.entities.enums.Season;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HarvestRepository extends JpaRepository<Harvest, Long> {
    boolean existsByFieldIdAndSeason(Long fieldId, Season season);
    
    boolean existsByHarvestDetailsTreeIdAndSeason(Long treeId, Season season);

    List<Harvest> findBySeason(Season seasonEnum);

    List<Harvest> findBySeasonAndFieldId(Season seasonEnum, Long fieldId);
}
