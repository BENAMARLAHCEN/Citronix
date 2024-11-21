package com.citronix.controllers;

import com.citronix.dto.HarvestDTO;
import com.citronix.dto.HarvestDetailDTO;
import com.citronix.services.HarvestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/harvests")
public class HarvestController {
    private final HarvestService harvestService;

    public HarvestController(HarvestService harvestService) {
        this.harvestService = harvestService;
    }

    @PostMapping
    public ResponseEntity<HarvestDTO> createHarvest(@RequestBody @Valid HarvestDTO harvestDTO) {
        HarvestDTO createdHarvest = harvestService.createHarvest(harvestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHarvest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HarvestDTO> getHarvestById(@PathVariable Long id) {
        HarvestDTO harvest = harvestService.getHarvestById(id);
        return ResponseEntity.ok(harvest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHarvest(@PathVariable Long id) {
        harvestService.deleteHarvest(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<HarvestDTO>> getAllHarvests() {
        List<HarvestDTO> harvests = harvestService.getAllHarvests();
        return ResponseEntity.ok(harvests);
    }

    @PostMapping("/details")
    public ResponseEntity<HarvestDetailDTO> addHarvestDetail(@RequestBody @Valid HarvestDetailDTO harvestDetailDTO) {
        HarvestDetailDTO createdHarvestDetail = harvestService.addHarvestDetail(harvestDetailDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHarvestDetail);
    }

    @DeleteMapping("/details/{id}")
    public ResponseEntity<Void> deleteHarvestDetail(@PathVariable Long id) {
        harvestService.deleteHarvestDetail(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<HarvestDetailDTO> getHarvestDetailById(@PathVariable Long id) {
        HarvestDetailDTO harvestDetail = harvestService.getHarvestDetailById(id);
        return ResponseEntity.ok(harvestDetail);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<List<HarvestDetailDTO>> getHarvestDetailsByHarvestId(@PathVariable Long id) {
        List<HarvestDetailDTO> harvestDetails = harvestService.getHarvestDetailsByHarvestId(id);
        return ResponseEntity.ok(harvestDetails);
    }

    // Suivre les récoltes par saison (printemps, été, automne, hiver).
    @GetMapping("/season/{season}")
    public ResponseEntity<List<HarvestDTO>> getHarvestsBySeason(@PathVariable String season) {
        List<HarvestDTO> harvests = harvestService.getHarvestsBySeason(season);
        return ResponseEntity.ok(harvests);
    }

    @GetMapping("/season/{season}/{fieldId}")
    public ResponseEntity<List<HarvestDTO>> getHarvestsBySeasonAndField(@PathVariable String season, @PathVariable Long fieldId) {
        List<HarvestDTO> harvests = harvestService.getHarvestsBySeasonAndField(season, fieldId);
        return ResponseEntity.ok(harvests);
    }
}