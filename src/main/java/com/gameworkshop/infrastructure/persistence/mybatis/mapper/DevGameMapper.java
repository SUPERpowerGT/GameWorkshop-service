package com.gameworkshop.infrastructure.persistence.mybatis.mapper;

import com.gameworkshop.domain.DevGame.model.DevGame;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

    @Select("SELECT COUNT(*) FROM dev_game WHERE developer_profile_id = #{developerId}")
    int countByDeveloperId(@Param("developerId") String developerId);

    int updateById(DevGame devGame);

    int deleteById(String id);
}
