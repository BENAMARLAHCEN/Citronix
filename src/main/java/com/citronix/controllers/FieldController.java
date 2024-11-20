package com.citronix.controllers;

import com.citronix.dto.FieldDTO;
import com.citronix.services.FieldService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fields")
public class FieldController {
    private final FieldService fieldService;

    public FieldController(FieldService fieldService) {
        this.fieldService = fieldService;
    }

    @PostMapping
    public ResponseEntity<FieldDTO> createField(@RequestBody @Valid FieldDTO fieldDTO) {
        FieldDTO createdField = fieldService.createField(fieldDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdField);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FieldDTO> updateField(@PathVariable Long id, @RequestBody @Valid FieldDTO fieldDTO) {
        FieldDTO updatedField = fieldService.updateField(id, fieldDTO);
        return ResponseEntity.ok(updatedField);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FieldDTO> getFieldById(@PathVariable Long id) {
        FieldDTO field = fieldService.getFieldById(id);
        return ResponseEntity.ok(field);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteField(@PathVariable Long id) {
        fieldService.deleteField(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<FieldDTO>> getAllFields() {
        List<FieldDTO> fields = fieldService.getAllFields();
        return ResponseEntity.ok(fields);
    }
}
