package com.citronix.controllers;

import com.citronix.dto.FieldDTO;
import com.citronix.dto.TreeDTO;
import com.citronix.exceptions.EntityNotFoundException;
import com.citronix.exceptions.GlobalExceptionHandler;
import com.citronix.services.FieldService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FieldControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FieldService fieldService;

    @InjectMocks
    private FieldController fieldController;

    private ObjectMapper objectMapper;
    private FieldDTO fieldDTO;
    private TreeDTO treeDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(fieldController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();

        // Initialize test data
        treeDTO = TreeDTO.builder()
                .id(1L)
                .fieldId(1L)
                .build();

        fieldDTO = FieldDTO.builder()
                .id(1L)
                .area(10.0)
                .farmId(1L)
                .trees(Collections.singletonList(treeDTO))
                .build();
    }

    @Nested
    @DisplayName("POST /api/fields")
    class CreateFieldTests {

        @Test
        @DisplayName("Should create field successfully")
        void createField_Success() throws Exception {
            // Arrange
            when(fieldService.createField(any(FieldDTO.class))).thenReturn(fieldDTO);

            // Act & Assert
            mockMvc.perform(post("/api/fields")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(fieldDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.area", is(10.0)))
                    .andExpect(jsonPath("$.farmId", is(1)))
                    .andExpect(jsonPath("$.trees", hasSize(1)));

            verify(fieldService).createField(any(FieldDTO.class));
        }

        @Test
        @DisplayName("Should return 400 when field area is negative")
        void createField_InvalidArea() throws Exception {
            // Arrange
            fieldDTO.setArea(-1.0);

            // Act & Assert
            mockMvc.perform(post("/api/fields")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(fieldDTO)))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

            verify(fieldService, never()).createField(any(FieldDTO.class));
        }

        @Test
        @DisplayName("Should return 400 when field exceeds farm capacity")
        void createField_ExceedsFarmCapacity() throws Exception {
            // Arrange
            when(fieldService.createField(any(FieldDTO.class)))
                    .thenThrow(new IllegalArgumentException("Field area exceeds farm capacity"));

            // Act & Assert
            mockMvc.perform(post("/api/fields")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(fieldDTO)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message", containsString("Field area exceeds farm capacity")));
        }
    }

    @Nested
    @DisplayName("PUT /api/fields/{id}")
    class UpdateFieldTests {

        @Test
        @DisplayName("Should update field successfully")
        void updateField_Success() throws Exception {
            // Arrange
            when(fieldService.updateField(eq(1L), any(FieldDTO.class))).thenReturn(fieldDTO);

            // Act & Assert
            mockMvc.perform(put("/api/fields/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(fieldDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.area", is(10.0)))
                    .andExpect(jsonPath("$.farmId", is(1)));

            verify(fieldService).updateField(eq(1L), any(FieldDTO.class));
        }

        @Test
        @DisplayName("Should return 404 when updating non-existent field")
        void updateField_NotFound() throws Exception {
            // Arrange
            when(fieldService.updateField(eq(1L), any(FieldDTO.class)))
                    .thenThrow(new EntityNotFoundException("Field not found"));

            // Act & Assert
            mockMvc.perform(put("/api/fields/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(fieldDTO)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", containsString("Field not found")));
        }

        @Test
        @DisplayName("Should return 400 when updating with invalid data")
        void updateField_InvalidData() throws Exception {
            // Arrange
            fieldDTO.setArea(-1.0);

            // Act & Assert
            mockMvc.perform(put("/api/fields/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(fieldDTO)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("GET /api/fields")
    class GetFieldsTests {

        @Test
        @DisplayName("Should return all fields successfully")
        void getAllFields_Success() throws Exception {
            // Arrange
            List<FieldDTO> fields = Arrays.asList(fieldDTO);
            when(fieldService.getAllFields()).thenReturn(fields);

            // Act & Assert
            mockMvc.perform(get("/api/fields"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id", is(1)))
                    .andExpect(jsonPath("$[0].area", is(10.0)))
                    .andExpect(jsonPath("$[0].farmId", is(1)));

            verify(fieldService).getAllFields();
        }

        @Test
        @DisplayName("Should return empty list when no fields exist")
        void getAllFields_Empty() throws Exception {
            // Arrange
            when(fieldService.getAllFields()).thenReturn(Collections.emptyList());

            // Act & Assert
            mockMvc.perform(get("/api/fields"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(fieldService).getAllFields();
        }
    }

    @Nested
    @DisplayName("GET /api/fields/{id}")
    class GetFieldByIdTests {

        @Test
        @DisplayName("Should return field by id successfully")
        void getFieldById_Success() throws Exception {
            // Arrange
            when(fieldService.getFieldById(1L)).thenReturn(fieldDTO);

            // Act & Assert
            mockMvc.perform(get("/api/fields/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.area", is(10.0)))
                    .andExpect(jsonPath("$.farmId", is(1)));

            verify(fieldService).getFieldById(1L);
        }

        @Test
        @DisplayName("Should return 404 when field not found")
        void getFieldById_NotFound() throws Exception {
            // Arrange
            when(fieldService.getFieldById(1L)).thenThrow(new EntityNotFoundException("Field not found"));

            // Act & Assert
            mockMvc.perform(get("/api/fields/1"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", containsString("Field not found")));

            verify(fieldService).getFieldById(1L);
        }
    }

    @Nested
    @DisplayName("DELETE /api/fields/{id}")
    class DeleteFieldTests {

        @Test
        @DisplayName("Should delete field successfully")
        void deleteField_Success() throws Exception {
            // Arrange
            doNothing().when(fieldService).deleteField(1L);

            // Act & Assert
            mockMvc.perform(delete("/api/fields/1"))
                    .andExpect(status().isNoContent());

            verify(fieldService).deleteField(1L);
        }

        @Test
        @DisplayName("Should return 404 when deleting non-existent field")
        void deleteField_NotFound() throws Exception {
            // Arrange
            doThrow(new EntityNotFoundException("Field not found"))
                    .when(fieldService).deleteField(1L);

            // Act & Assert
            mockMvc.perform(delete("/api/fields/1"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", containsString("Field not found")));

            verify(fieldService).deleteField(1L);
        }
    }
}