package com.citronix.mappers;

import com.citronix.dto.FarmDTO;
import com.citronix.dto.FieldDTO;
import com.citronix.entities.Farm;
import com.citronix.entities.Field;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FarmMapper {
     FarmDTO toFarmDTO(Farm farm);
     Farm toFarm(FarmDTO farmDTO);
     FieldDTO toFieldDTO(Field field);
     Field toField(FieldDTO fieldDTO);
}