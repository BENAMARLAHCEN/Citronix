package com.citronix.repositories;

import com.citronix.entities.HarvestDetail;
import com.citronix.entities.enums.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HarvestDetailRepository extends JpaRepository<HarvestDetail, Long> {
    @Query("SELECT CASE WHEN COUNT(h) > 0 THEN TRUE ELSE FALSE END " +
            "FROM HarvestDetail h " +
            "WHERE h.harvest.season = :season " +
            "AND YEAR(h.harvest.harvestDate) = :year " +
            "AND h.tree.id = :TreeId")
    boolean existsBySeasonAndHarvestDateYear(@Param("season") Season season, @Param("year") int year, @Param("TreeId") Long treeId);
}
