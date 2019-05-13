package com.server.draw.repository;

import com.server.draw.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;


public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query(value = "select * from jduser where wechatid = :userid", nativeQuery = true)
    UserEntity getByUserid(@Param("userid") String userid);


    @Query(value = "select * from jduser order by totalscore desc limit 7", nativeQuery = true)
    ArrayList<UserEntity> getTopByTotalScore();

}
