package com.citronix.services.impl;

import com.citronix.dto.TreeDTO;
import com.citronix.entities.Field;
import com.citronix.entities.Tree;
import com.citronix.exceptions.EntityNotFoundException;
import com.citronix.mappers.DtoMapper;
import com.citronix.repositories.FieldRepository;
import com.citronix.repositories.TreeRepository;
import com.citronix.services.TreeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TreeServiceImpl implements TreeService {
    private final TreeRepository treeRepository;
    private final FieldRepository fieldRepository;
    private final DtoMapper dtoMapper;

    public TreeServiceImpl(TreeRepository treeRepository, FieldRepository fieldRepository, DtoMapper modelMapper) {
        this.treeRepository = treeRepository;
        this.fieldRepository = fieldRepository;
        this.dtoMapper = modelMapper;
    }

    @Override
    public TreeDTO createTree(TreeDTO treeDTO) {
        Field field = fieldRepository.findById(treeDTO.getFieldId()).orElseThrow(() -> new EntityNotFoundException("Field not found"));
        Tree tree = dtoMapper.toTree(treeDTO);
        tree.setField(field);
        if (!tree.isPlantingSeasonValid()){
            throw new IllegalArgumentException("Planting date is not within the planting season");
        }
        tree = treeRepository.save(tree);
        return dtoMapper.toTreeDTO(tree);
    }

    @Override
    public TreeDTO updateTree(Long id, TreeDTO treeDTO) {
        Tree existingTree = treeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tree not found"));
        if (treeDTO.getFieldId() != null) {
            Field field = fieldRepository.findById(treeDTO.getFieldId()).orElseThrow(() -> new EntityNotFoundException("Field not found"));
            existingTree.setField(field);
        }
        if (treeDTO.getPlantingDate()!= null) {
            existingTree.setPlantingDate(treeDTO.getPlantingDate());
        }
        if (treeDTO.getIsProductive() != null) {
            existingTree.setIsProductive(treeDTO.getIsProductive());
        }
        Tree updatedTree = treeRepository.save(existingTree);
        return dtoMapper.toTreeDTO(updatedTree);
    }

    @Override
    public TreeDTO getTreeById(Long id) {
        Tree tree = treeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tree not found"));
        return dtoMapper.toTreeDTO(tree);
    }

    @Override
    public void deleteTree(Long id) {
        Tree tree = treeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tree not found"));
        treeRepository.delete(tree);
    }

    @Override
    public List<TreeDTO> getAllTrees() {
        List<Tree> trees = treeRepository.findAll();
        return trees.stream().map(tree -> dtoMapper.toTreeDTO(tree)).collect(Collectors.toList());
    }
}
