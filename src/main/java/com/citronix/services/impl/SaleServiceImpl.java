package com.citronix.services.impl;

import com.citronix.dto.SaleDTO;
import com.citronix.entities.Harvest;
import com.citronix.entities.Sale;
import com.citronix.exceptions.EntityNotFoundException;
import com.citronix.mappers.DtoMapper;
import com.citronix.repositories.HarvestRepository;
import com.citronix.repositories.SaleRepository;
import com.citronix.services.SaleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleServiceImpl implements SaleService {
    private final SaleRepository saleRepository;
    private final HarvestRepository harvestRepository;
    private final DtoMapper dtoMapper;

    public SaleServiceImpl(SaleRepository saleRepository, HarvestRepository harvestRepository, DtoMapper dtoMapper) {
        this.saleRepository = saleRepository;
        this.harvestRepository = harvestRepository;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public SaleDTO createSale(SaleDTO saleDTO) {
        Harvest harvest = harvestRepository.findById(saleDTO.getHarvestId())
                .orElseThrow(() -> new EntityNotFoundException("Harvest not found"));
        if (saleDTO.getQuantity() > harvest.getRemainingQuantity()) {
            throw new IllegalArgumentException("Quantity exceeds remaining harvest quantity");
        }
        harvest.updateRemainingQuantity(saleDTO.getQuantity());
        harvestRepository.save(harvest);
        Sale sale = dtoMapper.toSale(saleDTO);
        sale.setHarvest(harvest);
        sale = saleRepository.save(sale);
        return dtoMapper.toSaleDTO(sale);
    }

    @Override
    public SaleDTO updateSale(Long id, SaleDTO saleDTO) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sale not found"));
        Harvest harvest = harvestRepository.findById(saleDTO.getHarvestId())
                .orElseThrow(() -> new EntityNotFoundException("Harvest not found"));
        if (saleDTO.getQuantity() > harvest.getRemainingQuantity() + sale.getQuantity()) {
            throw new IllegalArgumentException("Quantity exceeds remaining harvest quantity");
        }
        harvest.updateRemainingQuantity(saleDTO.getQuantity() - sale.getQuantity());
        harvestRepository.save(harvest);
        sale.setQuantity(saleDTO.getQuantity());
        sale.setHarvest(harvest);
        sale = saleRepository.save(sale);
        return dtoMapper.toSaleDTO(sale);
    }

    @Override
    public SaleDTO getSaleById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sale not found"));
        return dtoMapper.toSaleDTO(sale);
    }

    @Override
    public void deleteSale(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sale not found"));
        Harvest harvest = sale.getHarvest();
        harvest.updateRemainingQuantity(-sale.getQuantity());
        harvestRepository.save(harvest);
        saleRepository.deleteById(id);
    }

    @Override
    public List<SaleDTO> getAllSales() {
        return saleRepository.findAll().stream()
                .map(dtoMapper::toSaleDTO)
                .toList();
    }

}
