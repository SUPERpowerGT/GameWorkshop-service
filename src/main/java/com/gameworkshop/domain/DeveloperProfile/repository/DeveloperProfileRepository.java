package com.gameworkshop.domain.DeveloperProfile.repository;

import com.gameworkshop.domain.DeveloperProfile.model.DeveloperProfile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeveloperProfileRepository {
    Optional<DeveloperProfile> findById(String id);

    Optional<DeveloperProfile> findByUserId(String userId);

    List<DeveloperProfile> findAll();

    void save(DeveloperProfile developerProfile);

    int batchSave(List<DeveloperProfile> profiles);

    int updateProjectCount(String id, Integer projectCount);

    int deleteById(String id);

    int deleteByUserId(String userId);

    long count();
}
