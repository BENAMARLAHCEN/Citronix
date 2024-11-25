package com.citronix.controllers;

import com.citronix.dto.FarmDTO;
import com.citronix.services.FarmService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farms")
public class FarmController {
    private final FarmService farmService;

    public FarmController(FarmService farmService) {
        this.farmService = farmService;
    }

    @PostMapping
    public ResponseEntity<FarmDTO> createFarm(@RequestBody @Valid FarmDTO farmDTO) {
        FarmDTO createdFarm = farmService.createFarm(farmDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFarm);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FarmDTO> updateFarm(@PathVariable Long id, @RequestBody @Valid FarmDTO farmDTO) {
        FarmDTO updatedFarm = farmService.updateFarm(id, farmDTO);
        return ResponseEntity.ok(updatedFarm);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FarmDTO> getFarmById(@PathVariable Long id) {
        FarmDTO farm = farmService.getFarmById(id);
        return ResponseEntity.ok(farm);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFarm(@PathVariable Long id) {
        farmService.deleteFarm(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<FarmDTO>> getAllFarms() {
        List<FarmDTO> farms = farmService.getAllFarms();
        return ResponseEntity.ok(farms);
    }

    @GetMapping("/search")
    public ResponseEntity<List<FarmDTO>> searchFarms(@RequestParam(required = false) String name,
                                                     @RequestParam(required = false) String location,
                                                     @RequestParam(required = false) Double totalArea) {
        List<FarmDTO> farms = farmService.searchFarms(name, location, totalArea);
        return ResponseEntity.ok(farms);
    }
}