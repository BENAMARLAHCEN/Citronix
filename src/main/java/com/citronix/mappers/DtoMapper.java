package com.citronix.mappers;

import com.citronix.dto.FarmDTO;
import com.citronix.dto.FieldDTO;
import com.citronix.dto.TreeDTO;
import com.citronix.entities.Farm;
import com.citronix.entities.Field;
import com.citronix.entities.Tree;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DtoMapper {
    FarmDTO toFarmDTO(Farm farm);
    Farm toFarm(FarmDTO farmDTO);

    @Mapping(target = "farmId", source = "farm.id")
    FieldDTO toFieldDTO(Field field);

    Field toField(FieldDTO fieldDTO);

    Tree toTree(TreeDTO treeDTO);

    @Mapping(target = "fieldId", source = "field.id")
    TreeDTO toTreeDTO(Tree tree);
}