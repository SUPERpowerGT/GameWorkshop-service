package com.gameworkshop.domain.project.repository;

import com.gameworkshop.domain.project.model.DeveloperProfile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeveloperProfileMapper {
    DeveloperProfile findById(String id);
    void insert(DeveloperProfile developerProfile);
}
