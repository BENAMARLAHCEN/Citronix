package com.citronix.services.impl;

import com.citronix.dto.FarmDTO;
import com.citronix.entities.Farm;
import com.citronix.exceptions.EntityNotFoundException;
import com.citronix.mappers.FarmMapper;
import com.citronix.repositories.FarmRepository;
import com.citronix.services.FarmService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FarmServiceImpl implements FarmService {

    private FarmRepository farmRepository;
    private FarmMapper farmMapper;

    public FarmServiceImpl(FarmRepository farmRepository, FarmMapper farmMapper) {
        this.farmRepository = farmRepository;
        this.farmMapper = farmMapper;
    }

    @Override
    public FarmDTO createFarm(FarmDTO farmDTO) {
        Farm farm = farmMapper.toFarm(farmDTO);
        Farm savedFarm = farmRepository.save(farm);
        return farmMapper.toFarmDTO(savedFarm);
    }

    @Override
    public FarmDTO updateFarm(Long id, FarmDTO farmDTO) {
        Farm existingFarm = farmRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Farm not found"));
        existingFarm.setName(farmDTO.getName());
        existingFarm.setLocation(farmDTO.getLocation());
        existingFarm.setTotalArea(farmDTO.getTotalArea());
        existingFarm.setCreationDate(farmDTO.getCreationDate());
        Farm updatedFarm = farmRepository.save(existingFarm);
        return farmMapper.toFarmDTO(updatedFarm);
    }

    @Override
    public FarmDTO getFarmById(Long id) {
        Farm farm = farmRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Farm not found"));
        return farmMapper.toFarmDTO(farm);
    }

    @Override
    public List<FarmDTO> getAllFarms() {
        List<Farm> farms = farmRepository.findAll();
        return farms.stream().map(farm -> farmMapper.toFarmDTO(farm))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteFarm(Long id) {
        if (!farmRepository.existsById(id)) {
            throw new EntityNotFoundException("Farm not found");
        }
        farmRepository.deleteById(id);
    }

    @Override
    public Double calculateRemainingArea(Long farmId) {
        Farm farm = farmRepository.findById(farmId)
                .orElseThrow(() -> new EntityNotFoundException("Farm not found"));
        return farm.calculateRemainingArea();
    }
}