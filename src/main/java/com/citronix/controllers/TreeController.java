package com.citronix.controllers;

import com.citronix.dto.TreeDTO;
import com.citronix.services.TreeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trees")
public class TreeController {
    private final TreeService treeService;

    public TreeController(TreeService treeService) {
        this.treeService = treeService;
    }

    @PostMapping
    public ResponseEntity<TreeDTO> createTree(@RequestBody @Valid TreeDTO treeDTO) {
        TreeDTO createdTree = treeService.createTree(treeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTree);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TreeDTO> updateTree(@PathVariable Long id, @RequestBody @Valid TreeDTO treeDTO) {
        TreeDTO updatedTree = treeService.updateTree(id, treeDTO);
        return ResponseEntity.ok(updatedTree);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TreeDTO> getTreeById(@PathVariable Long id) {
        TreeDTO tree = treeService.getTreeById(id);
        return ResponseEntity.ok(tree);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTree(@PathVariable Long id) {
        treeService.deleteTree(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TreeDTO>> getAllTrees() {
        List<TreeDTO> trees = treeService.getAllTrees();
        return ResponseEntity.ok(trees);
    }

    @GetMapping("/field/{fieldId}")
    public ResponseEntity<List<TreeDTO>> getTreesByFieldId(@PathVariable Long fieldId) {
        List<TreeDTO> trees = treeService.getTreesByFieldId(fieldId);
        return ResponseEntity.ok(trees);
    }
}