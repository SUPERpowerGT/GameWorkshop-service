package com.gameworkshop.infrastructure.persistence.mybatis.mapper;

import com.gameworkshop.domain.DeveloperProfile.model.DeveloperProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Optional;

@Mapper
public interface DeveloperProfileMapper {
    /**
     * find developerProfile by id
     * param: id
     * return: developerProfile object
     */
    Optional<DeveloperProfile> findById(String id);


    @Update("UPDATE developer_profile " +
            "SET project_count = #{count} " +
            "WHERE id = #{developerId}")
    void updateProjectCount(@Param("developerId") String developerId,
                           @Param("count") int count);

    /**
     * find developerProfile by userid
     * param: userid
     * return: developerProfile object
     */
    Optional<DeveloperProfile> findByUserId(String userId);

    /**
     * find all developerProfile
     * return: all developerProfile object
     */
    List<DeveloperProfile> findAll();

    /**
     * add new developerProfile and return status of this function
     * param: developerProfile
     * return: status right is 1,false is 0
     */
    void insert(DeveloperProfile developerProfile);

    /**
     * add more developerProfiles and return status of this function at the same times
     * param: developerProfiles
     * return: status right is 1,false is 0
     */
    int batchInsert(@Param("profiles") List<DeveloperProfile> profiles);


    int updateById(DeveloperProfile developerProfile);

    int updateProjectCountById(@Param("id") String id, @Param("projectCount") Integer projectCount);

    int deleteById(String id);

    int deleteByUserId(String userId);

    long count();
}
