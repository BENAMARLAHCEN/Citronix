package com.citronix.services.impl;

import com.citronix.dto.HarvestDTO;
import com.citronix.dto.HarvestDetailDTO;
import com.citronix.entities.Harvest;
import com.citronix.entities.HarvestDetail;
import com.citronix.entities.Tree;
import com.citronix.exceptions.EntityNotFoundException;
import com.citronix.mappers.DtoMapper;
import com.citronix.repositories.FieldRepository;
import com.citronix.repositories.HarvestDetailRepository;
import com.citronix.repositories.HarvestRepository;
import com.citronix.repositories.TreeRepository;
import com.citronix.services.HarvestService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HarvestServiceImpl implements HarvestService {
    private final HarvestRepository harvestRepository;
    private final TreeRepository treeRepository;
    private final FieldRepository fieldRepository;
    private final HarvestDetailRepository harvestDetailRepository;
    private final DtoMapper dtoMapper;

    public HarvestServiceImpl(HarvestRepository harvestRepository, TreeRepository treeRepository, FieldRepository fieldRepository, HarvestDetailRepository harvestDetailRepository, DtoMapper dtoMapper) {
        this.harvestRepository = harvestRepository;
        this.treeRepository = treeRepository;
        this.fieldRepository = fieldRepository;
        this.harvestDetailRepository = harvestDetailRepository;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public HarvestDTO createHarvest(HarvestDTO harvestDTO) {
        Harvest harvest = dtoMapper.toHarvest(harvestDTO);
        if(harvestRepository.existsByFieldIdAndSeasonAndHarvestDate(harvestDTO.getFieldId(), harvest.getSeason(), harvestDTO.getHarvestDate())) {
            throw new RuntimeException("Harvest already exists for this field and season");
        }
        List<HarvestDetail> harvestDetails = new ArrayList<>();
        if (harvestDTO.getHarvestDetails() != null) {
            for (HarvestDetailDTO detailDTO : harvestDTO.getHarvestDetails()) {
                Tree tree = treeRepository.findById(detailDTO.getTreeId()).orElseThrow(() -> new EntityNotFoundException("Tree not found"));
                if (tree.getField().getId() != harvestDTO.getFieldId()) {
                    throw new IllegalStateException("The tree does not belong to the field.");
                }
                if (harvestRepository.existsByHarvestDetailsTreeIdAndSeason(tree.getId(), harvest.getSeason())) {
                    throw new IllegalStateException("The tree has already been harvested this season.");
                }
                HarvestDetail harvestDetail = dtoMapper.toHarvestDetail(detailDTO);
                harvestDetail.setTree(tree);
                harvestDetail.setQuantity(tree.getProductivity());
                harvestDetails.add(harvestDetail);
            }
        }
        harvest.setField(fieldRepository.findById(harvestDTO.getFieldId()).orElseThrow(() -> new EntityNotFoundException("Field not found")));
        harvest.setTotalQuantity(harvestDetails.stream().mapToDouble(HarvestDetail::getQuantity).sum());
        harvest.setRemainingQuantity(harvest.getTotalQuantity());
        harvest.setHarvestDetails(null);
        Harvest savedHarvest = harvestRepository.save(harvest);
        if (!harvestDetails.isEmpty()) {
            harvestDetails.forEach(harvestDetail -> harvestDetail.setHarvest(savedHarvest));
            harvestDetailRepository.saveAll(harvestDetails);
            savedHarvest.setHarvestDetails(harvestDetails);
        }
        return dtoMapper.toHarvestDTO(savedHarvest);
    }

    public HarvestDTO getHarvestById(Long id) {
        Harvest harvest = harvestRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Harvest not found"));
        return dtoMapper.toHarvestDTO(harvest);
    }

    public void deleteHarvest(Long id) {
        if (!harvestRepository.existsById(id)) {
            throw new EntityNotFoundException("Harvest not found");
        }
        harvestRepository.deleteById(id);
    }

    public List<HarvestDTO> getAllHarvests() {
        List<Harvest> harvests = harvestRepository.findAll();
        return harvests.stream().map(dtoMapper::toHarvestDTO).collect(Collectors.toList());
    }

    public HarvestDetailDTO addHarvestDetail(HarvestDetailDTO harvestDetailDTO) {
        Harvest harvest = harvestRepository.findById(harvestDetailDTO.getHarvestId()).orElseThrow(() -> new EntityNotFoundException("Harvest not found"));
        Tree tree = treeRepository.findById(harvestDetailDTO.getTreeId()).orElseThrow(() -> new EntityNotFoundException("Tree not found"));
        if (tree.getField().getId() != harvest.getField().getId()) {
            throw new IllegalStateException("The tree does not belong to the field.");
        }
        if (harvestRepository.existsByHarvestDetailsTreeIdAndSeason(tree.getId(), harvest.getSeason())) {
            throw new IllegalStateException("The tree has already been harvested this season.");
        }
        HarvestDetail harvestDetail = dtoMapper.toHarvestDetail(harvestDetailDTO);
        harvestDetail.setTree(tree);
        harvestDetail.setQuantity(tree.getProductivity());
        harvestDetail.setHarvest(harvest);
        harvestDetailRepository.save(harvestDetail);
        harvest.getHarvestDetails().add(harvestDetail);
        harvest.setTotalQuantity(harvest.getHarvestDetails().stream().mapToDouble(HarvestDetail::getQuantity).sum());
        harvest.setRemainingQuantity(harvest.getTotalQuantity() - harvest.getHarvestDetails().stream().mapToDouble(HarvestDetail::getQuantity).sum());
        harvestRepository.save(harvest);
        return dtoMapper.toHarvestDetailDTO(harvestDetail);
    }

    public void deleteHarvestDetail(Long id) {
        HarvestDetail harvestDetail = harvestDetailRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Harvest detail not found"));
        Harvest harvest = harvestDetail.getHarvest();
        harvest.getHarvestDetails().remove(harvestDetail);
        harvest.setTotalQuantity(harvest.getHarvestDetails().stream().mapToDouble(HarvestDetail::getQuantity).sum());
        harvest.setRemainingQuantity(harvest.getTotalQuantity() - harvest.getHarvestDetails().stream().mapToDouble(HarvestDetail::getQuantity).sum());
        harvestRepository.save(harvest);
        harvestDetailRepository.delete(harvestDetail);
    }

    public List<HarvestDetailDTO> getHarvestDetailsByHarvestId(Long id) {
        Harvest harvest = harvestRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Harvest not found"));
        return harvest.getHarvestDetails().stream().map(dtoMapper::toHarvestDetailDTO).collect(Collectors.toList());
    }

    public HarvestDetailDTO getHarvestDetailById(Long id) {
        HarvestDetail harvestDetail = harvestDetailRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Harvest detail not found"));
        return dtoMapper.toHarvestDetailDTO(harvestDetail);
    }
}
