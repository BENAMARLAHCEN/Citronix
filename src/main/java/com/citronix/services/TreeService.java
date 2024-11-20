package com.citronix.services;

import com.citronix.dto.TreeDTO;

import java.util.List;

public interface TreeService {
    TreeDTO createTree(TreeDTO treeDTO);
    TreeDTO updateTree(Long id, TreeDTO treeDTO);
    TreeDTO getTreeById(Long id);
    void deleteTree(Long id);
    List<TreeDTO> getAllTrees();
}
