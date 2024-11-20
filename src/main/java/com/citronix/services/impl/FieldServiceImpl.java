package com.citronix.services.impl;

import com.citronix.dto.FieldDTO;
import com.citronix.entities.Farm;
import com.citronix.entities.Field;
import com.citronix.exceptions.EntityNotFoundException;
import com.citronix.mappers.FieldMapper;
import com.citronix.repositories.FarmRepository;
import com.citronix.repositories.FieldRepository;
import com.citronix.services.FieldService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FieldServiceImpl implements FieldService {
    private final FieldRepository fieldRepository;
    private final FarmRepository farmRepository;
    private final FieldMapper fieldMapper;

    public FieldServiceImpl(FieldRepository fieldRepository, FarmRepository farmRepository, FieldMapper fieldMapper) {
        this.fieldRepository = fieldRepository;
        this.farmRepository = farmRepository;
        this.fieldMapper = fieldMapper;
    }

    @Override
    public FieldDTO createField(FieldDTO fieldDTO) {
        Farm farm = farmRepository.findById(fieldDTO.getFarmId()).orElseThrow(() -> new EntityNotFoundException("Farm not found"));
        Field field = fieldMapper.toField(fieldDTO);
        field.setFarm(farm);
        field = fieldRepository.save(field);
        return fieldMapper.toFieldDTO(field);
    }

    @Override
    public FieldDTO updateField(Long id, FieldDTO fieldDTO) {
        Field existingField = fieldRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Field not found"));
        Field nowUpdateField = fieldMapper.toField(fieldDTO);
        if (fieldDTO.getFarmId() != null) {
            Farm farm = farmRepository.findById(fieldDTO.getFarmId()).orElseThrow(() -> new EntityNotFoundException("Farm not found"));
            nowUpdateField.setFarm(farm);
        }
        if (fieldDTO.getArea() != null) {
            existingField.setArea(nowUpdateField.getArea());
        }
        if (fieldDTO.getTrees() != null) {
            existingField.setTrees(nowUpdateField.getTrees());
        }
        Field updatedField = fieldRepository.save(existingField);
        return fieldMapper.toFieldDTO(updatedField);
    }

    @Override
    public FieldDTO getFieldById(Long id) {
        Field field = fieldRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Field not found"));
        return fieldMapper.toFieldDTO(field);
    }

    @Override
    public void deleteField(Long id) {
        Field field = fieldRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Field not found"));
        fieldRepository.delete(field);
    }

    @Override
    public List<FieldDTO> getAllFields() {
        List<Field> fields = fieldRepository.findAll();
        return fields.stream().map(field -> fieldMapper.toFieldDTO(field)).collect(Collectors.toList());
    }
}
