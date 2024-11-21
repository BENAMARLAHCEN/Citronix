package com.citronix.services;

import com.citronix.dto.HarvestDTO;
import com.citronix.dto.HarvestDetailDTO;

import java.util.List;

public interface HarvestService {
    HarvestDTO createHarvest(HarvestDTO harvestDTO);
//    HarvestDTO updateHarvest(Long id, HarvestDTO harvestDTO);
    HarvestDTO getHarvestById(Long id);
    void deleteHarvest(Long id);
    List<HarvestDTO> getAllHarvests();
    HarvestDetailDTO addHarvestDetail(HarvestDetailDTO harvestDetailDTO);
    void deleteHarvestDetail(Long id);
    List<HarvestDetailDTO> getHarvestDetailsByHarvestId(Long id);
    HarvestDetailDTO getHarvestDetailById(Long id);
}
