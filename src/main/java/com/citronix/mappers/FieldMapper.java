package com.citronix.mappers;

import com.citronix.dto.FieldDTO;
import com.citronix.dto.TreeDTO;
import com.citronix.entities.Field;
import com.citronix.entities.Tree;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FieldMapper {
     @Mapping(target = "farmId", source = "farm.id")
     FieldDTO toFieldDTO(Field field);
     Field toField(FieldDTO fieldDTO);
     TreeDTO toTreeDTO(Tree tree);
     Tree toTree(TreeDTO treeDTO);
}