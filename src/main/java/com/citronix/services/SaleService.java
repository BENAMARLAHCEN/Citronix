package com.citronix.services;

import com.citronix.dto.SaleDTO;

import java.util.List;

public interface SaleService {
    SaleDTO createSale(SaleDTO saleDTO);
    SaleDTO updateSale(Long id, SaleDTO saleDTO);
    SaleDTO getSaleById(Long id);
    void deleteSale(Long id);
    List<SaleDTO> getAllSales();
}
