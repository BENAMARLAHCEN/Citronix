package com.citronix.services;

import com.citronix.dto.FieldDTO;

import java.util.List;

public interface FieldService {
    FieldDTO createField(FieldDTO fieldDTO);
    FieldDTO updateField(Long id, FieldDTO fieldDTO);
    FieldDTO getFieldById(Long id);
    void deleteField(Long id);
    List<FieldDTO> getAllFields();
}