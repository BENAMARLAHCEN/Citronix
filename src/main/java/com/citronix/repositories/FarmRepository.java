package com.citronix.repositories;

import com.citronix.entities.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FarmRepository extends JpaRepository<Farm, Long> {

    @Query("SELECT f FROM Farm f WHERE (:name IS NULL OR f.name = :name) AND (:location IS NULL OR f.location = :location) AND (:totalArea IS NULL OR f.totalArea = :totalArea)")
    List<Farm> searchFarms(@Param("name") String name, @Param("location") String location, @Param("totalArea") Double totalArea);
}