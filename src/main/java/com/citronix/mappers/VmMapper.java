package com.citronix.mappers;

import com.citronix.entities.Farm;
import com.citronix.entities.Field;
import com.citronix.entities.Tree;
import com.citronix.viewmodel.FarmVM;
import com.citronix.viewmodel.FieldVM;
import com.citronix.viewmodel.TreeVM;
import org.mapstruct.Mapper;

@Mapper
public interface VmMapper {
    FarmVM toFarmVM(Farm farm);

    TreeVM toTreeVM(Tree tree);

    FieldVM toFieldVM(Field field);
}
