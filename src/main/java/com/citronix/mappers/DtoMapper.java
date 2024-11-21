package com.citronix.mappers;

import com.citronix.dto.*;
import com.citronix.entities.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DtoMapper {
    FarmDTO toFarmDTO(Farm farm);
    Farm toFarm(FarmDTO farmDTO);

    @Mapping(target = "farmId", source = "farm.id")
    FieldDTO toFieldDTO(Field field);

    @Mapping(target = "farm.id", source = "farmId")
    Field toField(FieldDTO fieldDTO);

    Tree toTree(TreeDTO treeDTO);

    @Mapping(target = "fieldId", source = "field.id")
    TreeDTO toTreeDTO(Tree tree);

    @Mapping(target = "fieldId", source = "field.id")
    HarvestDTO toHarvestDTO(Harvest harvest);

    @Mapping(target = "field.id", source = "fieldId")
    Harvest toHarvest(HarvestDTO harvestDTO);

    @Mapping(target = "harvestId", source = "harvest.id")
    @Mapping(target = "treeId", source = "tree.id")
    HarvestDetailDTO toHarvestDetailDTO(HarvestDetail harvestDetail);

    @Mapping(target = "harvest.id", source = "harvestId")
    @Mapping(target = "tree.id", source = "treeId")
    HarvestDetail toHarvestDetail(HarvestDetailDTO harvestDetailDTO);

    @Mapping(target = "harvestId", source = "harvest.id")
    SaleDTO toSaleDTO(Sale sale);

    Sale toSale(SaleDTO saleDTO);
}