package com.citronix.repositories;

import com.citronix.entities.HarvestDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HarvestDetailRepository extends JpaRepository<HarvestDetail, Long> {
    List<HarvestDetail> findByHarvestId(Long harvestId);
    void deleteByHarvestId(Long harvestId);
}
