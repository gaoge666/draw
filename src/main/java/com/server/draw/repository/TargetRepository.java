package com.server.draw.repository;

import com.server.draw.model.TargetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface TargetRepository extends JpaRepository<TargetEntity, Long> {

    @Query(value = "select * from target where name not in :excludedTargets and level = :difflevel limit 4", nativeQuery = true)
    ArrayList<TargetEntity> getByLevel(@Param("difflevel") int difflevel, @Param("excludedTargets") ArrayList<String> excludedTargets);


    @Query(value = "select * from target where name = :targetName", nativeQuery =  true)
    TargetEntity getByName(@Param("targetName") String targetName);

//    @Query(value = "select * from target where level = :difflevel", nativeQuery = true)
//    public TargetEntity getByLevel(@Param("difflevel") int difflevel);



}
