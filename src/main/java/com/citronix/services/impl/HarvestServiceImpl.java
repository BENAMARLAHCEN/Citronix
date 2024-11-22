package com.citronix.services.impl;

import com.citronix.dto.HarvestDTO;
import com.citronix.dto.HarvestDetailDTO;
import com.citronix.entities.Harvest;
import com.citronix.entities.HarvestDetail;
import com.citronix.entities.Tree;
import com.citronix.entities.enums.Season;
import com.citronix.exceptions.EntityNotFoundException;
import com.citronix.mappers.DtoMapper;
import com.citronix.repositories.FieldRepository;
import com.citronix.repositories.HarvestDetailRepository;
import com.citronix.repositories.HarvestRepository;
import com.citronix.repositories.TreeRepository;
import com.citronix.services.HarvestService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HarvestServiceImpl implements HarvestService {
    private static final Map<Season, int[]> SEASON_MONTH_RANGES = new HashMap<>();

    static {
        SEASON_MONTH_RANGES.put(Season.AUTUMN, new int[]{9, 12});
        SEASON_MONTH_RANGES.put(Season.SPRING, new int[]{3, 6});
        SEASON_MONTH_RANGES.put(Season.SUMMER, new int[]{6, 9});
        SEASON_MONTH_RANGES.put(Season.WINTER, new int[]{12, 3});
    }

    private void validateHarvestDate(Harvest harvest) {
        int month = harvest.getHarvestDate().getMonthValue();
        int[] range = SEASON_MONTH_RANGES.get(harvest.getSeason());

        if (range[0] < range[1]) {
            if (month < range[0] || month > range[1]) {
                throw new IllegalStateException(harvest.getSeason() + " harvest can only be done between " + getMonthName(range[0]) + " and " + getMonthName(range[1]));
            }
        } else {
            if (month < range[0] && month > range[1]) {
                throw new IllegalStateException(harvest.getSeason() + " harvest can only be done between " + getMonthName(range[0]) + " and " + getMonthName(range[1]));
            }
        }
    }

    private String getMonthName(int month) {
        return java.time.Month.of(month).name();
    }

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
        validateHarvestDate(harvest);
        List<HarvestDetail> harvestDetails = new ArrayList<>();
        if (harvestDTO.getHarvestDetails() != null) {
            for (HarvestDetailDTO detailDTO : harvestDTO.getHarvestDetails()) {
                Tree tree = treeRepository.findById(detailDTO.getTreeId()).orElseThrow(() -> new EntityNotFoundException("Tree not found"));
                if(harvestDetailRepository.existsBySeasonAndHarvestDateYear(harvest.getSeason(), harvest.getHarvestDate().getYear(), tree.getId())) {
                    throw new IllegalStateException("The tree has already been harvested this season.");
                }
                HarvestDetail harvestDetail = dtoMapper.toHarvestDetail(detailDTO);
                harvestDetail.setTree(tree);
                harvestDetail.setQuantity(tree.getProductivity());
                harvestDetails.add(harvestDetail);
            }
        }
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
        if(harvestDetailRepository.existsBySeasonAndHarvestDateYear(harvest.getSeason(), harvest.getHarvestDate().getYear(), tree.getId())) {
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

    public List<HarvestDTO> getHarvestsBySeason(String season) {
        Season seasonEnum = Season.valueOf(season.toUpperCase());
        List<Harvest> harvests = harvestRepository.findBySeason(seasonEnum);
        return harvests.stream().map(dtoMapper::toHarvestDTO).collect(Collectors.toList());
    }
}
