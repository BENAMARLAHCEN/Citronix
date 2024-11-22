package com.citronix.services.impl;

import com.citronix.dto.FieldDTO;
import com.citronix.dto.TreeDTO;
import com.citronix.entities.Farm;
import com.citronix.entities.Field;
import com.citronix.entities.Tree;
import com.citronix.exceptions.EntityNotFoundException;
import com.citronix.mappers.FieldMapper;
import com.citronix.repositories.FarmRepository;
import com.citronix.repositories.FieldRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FieldServiceImplTest {

    @Mock
    private FieldRepository fieldRepository;

    @Mock
    private FarmRepository farmRepository;

    @Mock
    private FieldMapper fieldMapper;

    @InjectMocks
    private FieldServiceImpl fieldService;

    private Farm farm;
    private Field field;
    private FieldDTO fieldDTO;
    private Tree tree;
    private TreeDTO treeDTO;

    @BeforeEach
    void setUp() {
        // Initialize test data
        farm = Farm.builder()
                .id(1L)
                .totalArea(100.0)
                .fields(new ArrayList<>())
                .build();

        field = Field.builder()
                .id(1L)
                .area(10.0)
                .farm(farm)
                .trees(new ArrayList<>())
                .build();

        tree = Tree.builder()
                .id(1L)
                .field(field)
                .build();

        fieldDTO = FieldDTO.builder()
                .id(1L)
                .area(10.0)
                .farmId(1L)
                .trees(new ArrayList<>())
                .build();

        treeDTO = TreeDTO.builder()
                .id(1L)
                .fieldId(1L)
                .build();
    }

    @Nested
    @DisplayName("Create Field Tests")
    class CreateFieldTests {

        @Test
        @DisplayName("Should successfully create field when all conditions are met")
        void createField_Success() {
            // Arrange
            when(farmRepository.findById(1L)).thenReturn(Optional.of(farm));
            when(fieldMapper.toField(fieldDTO)).thenReturn(field);
            when(fieldRepository.save(any(Field.class))).thenReturn(field);
            when(fieldMapper.toFieldDTO(field)).thenReturn(fieldDTO);

            // Act
            FieldDTO result = fieldService.createField(fieldDTO);

            // Assert
            assertNotNull(result);
            assertEquals(fieldDTO.getArea(), result.getArea());
            verify(fieldRepository).save(any(Field.class));
        }

        @Test
        @DisplayName("Should throw exception when farm has maximum fields")
        void createField_MaxFieldsExceeded() {
            // Arrange
            List<Field> existingFields = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                existingFields.add(new Field());
            }
            farm.setFields(existingFields);
            when(farmRepository.findById(1L)).thenReturn(Optional.of(farm));
            when(fieldMapper.toField(fieldDTO)).thenReturn(field);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> fieldService.createField(fieldDTO));
            verify(fieldRepository, never()).save(any(Field.class));
        }

        @Test
        @DisplayName("Should throw exception when field area exceeds farm's remaining area")
        void createField_ExceedingFarmArea() {
            // Arrange
            fieldDTO.setArea(150.0); // Greater than farm's total area
            field.setArea(150.0);
            when(farmRepository.findById(1L)).thenReturn(Optional.of(farm));
            when(fieldMapper.toField(fieldDTO)).thenReturn(field);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> fieldService.createField(fieldDTO));
            verify(fieldRepository, never()).save(any(Field.class));
        }

        @Test
        @DisplayName("Should throw exception when field area exceeds 50% of farm area")
        void createField_ExceedingMaxFieldArea() {
            // Arrange
            fieldDTO.setArea(51.0); // More than 50% of farm's total area
            field.setArea(51.0);
            when(farmRepository.findById(1L)).thenReturn(Optional.of(farm));
            when(fieldMapper.toField(fieldDTO)).thenReturn(field);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> fieldService.createField(fieldDTO));
            verify(fieldRepository, never()).save(any(Field.class));
        }
    }

    @Nested
    @DisplayName("Update Field Tests")
    class UpdateFieldTests {

        @Test
        @DisplayName("Should successfully update field when all conditions are met")
        void updateField_Success() {
            // Arrange
            when(fieldRepository.findById(1L)).thenReturn(Optional.of(field));
            when(farmRepository.findById(1L)).thenReturn(Optional.of(farm));
            when(fieldRepository.save(any(Field.class))).thenReturn(field);
            when(fieldMapper.toField(fieldDTO)).thenReturn(field);
            when(fieldMapper.toFieldDTO(field)).thenReturn(fieldDTO);

            // Act
            FieldDTO result = fieldService.updateField(1L, fieldDTO);

            // Assert
            assertNotNull(result);
            assertEquals(fieldDTO.getArea(), result.getArea());
            verify(fieldRepository).save(any(Field.class));
        }

        @Test
        @DisplayName("Should throw exception when updating non-existent field")
        void updateField_FieldNotFound() {
            // Arrange
            when(fieldRepository.findById(1L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(EntityNotFoundException.class, () -> fieldService.updateField(1L, fieldDTO));
            verify(fieldRepository, never()).save(any(Field.class));
        }

        @Test
        @DisplayName("Should throw exception when updating field area exceeds farm's capacity")
        void updateField_ExceedingFarmCapacity() {
            // Arrange
            fieldDTO.setArea(150.0);
            Field newField = field;
            newField.setArea(150.0);
            when(fieldRepository.findById(1L)).thenReturn(Optional.of(field));
            when(farmRepository.findById(1L)).thenReturn(Optional.of(farm)); // Ensure farm is found
            when(fieldMapper.toField(fieldDTO)).thenReturn(newField);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> fieldService.updateField(1L, fieldDTO));
            verify(fieldRepository, never()).save(any(Field.class));
        }
    }

    @Nested
    @DisplayName("Get Field Tests")
    class GetFieldTests {

        @Test
        @DisplayName("Should successfully retrieve field by ID")
        void getFieldById_Success() {
            // Arrange
            when(fieldRepository.findById(1L)).thenReturn(Optional.of(field));
            when(fieldMapper.toFieldDTO(field)).thenReturn(fieldDTO);

            // Act
            FieldDTO result = fieldService.getFieldById(1L);

            // Assert
            assertNotNull(result);
            assertEquals(fieldDTO.getId(), result.getId());
        }

        @Test
        @DisplayName("Should throw exception when getting non-existent field")
        void getFieldById_NotFound() {
            // Arrange
            when(fieldRepository.findById(1L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(EntityNotFoundException.class, () -> fieldService.getFieldById(1L));
        }

        @Test
        @DisplayName("Should successfully retrieve all fields")
        void getAllFields_Success() {
            // Arrange
            List<Field> fields = Arrays.asList(field);
            List<FieldDTO> fieldDTOs = Arrays.asList(fieldDTO);
            when(fieldRepository.findAll()).thenReturn(fields);
            when(fieldMapper.toFieldDTO(field)).thenReturn(fieldDTO);

            // Act
            List<FieldDTO> result = fieldService.getAllFields();

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(fieldDTOs.get(0).getId(), result.get(0).getId());
        }
    }

    @Nested
    @DisplayName("Delete Field Tests")
    class DeleteFieldTests {

        @Test
        @DisplayName("Should successfully delete existing field")
        void deleteField_Success() {
            // Arrange
            when(fieldRepository.findById(1L)).thenReturn(Optional.of(field));
            doNothing().when(fieldRepository).delete(field);

            // Act
            fieldService.deleteField(1L);

            // Assert
            verify(fieldRepository).delete(field);
        }

        @Test
        @DisplayName("Should throw exception when deleting non-existent field")
        void deleteField_NotFound() {
            // Arrange
            when(fieldRepository.findById(1L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(EntityNotFoundException.class, () -> fieldService.deleteField(1L));
            verify(fieldRepository, never()).delete(any(Field.class));
        }
    }
}