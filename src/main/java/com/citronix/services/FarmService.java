package com.citronix.services;

import com.citronix.dto.FarmDTO;

import java.util.List;

public interface FarmService {
    FarmDTO createFarm(FarmDTO farmDTO);
    FarmDTO updateFarm(Long id, FarmDTO farmDTO);
    FarmDTO getFarmById(Long id);
    List<FarmDTO> getAllFarms();
    void deleteFarm(Long id);
    Double calculateRemainingArea(Long farmId);
}