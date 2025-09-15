package com.gameworkshop.infrastructure.persistence.mybatis.mapper;

import com.gameworkshop.domain.DevGame.model.DevGame;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DevGameMapper {
    int insert(DevGame devGame);

    /**
     * selected game ById
     * param: id
     * return: game object
     */
    DevGame selectById(String id);

    /**
     * select game by developer id
     * param: developerProfileId
     * return: List of all game
     */
    List<DevGame> selectByDeveloperProfileId(@Param("developerProfileId") String developerProfileId);

    int updateById(DevGame devGame);

    int deleteById(String id);
}
