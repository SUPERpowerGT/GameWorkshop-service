package com.gameworkshop.infrastructure.persistence.mybatis.repositoryImpl;

import com.gameworkshop.domain.DeveloperProfile.model.DeveloperProfile;
import com.gameworkshop.domain.DeveloperProfile.repository.DeveloperProfileRepository;
import com.gameworkshop.infrastructure.persistence.mybatis.mapper.DevGameMapper;
import com.gameworkshop.infrastructure.persistence.mybatis.mapper.DeveloperProfileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DeveloperProfileRepositoryImpl implements DeveloperProfileRepository {
    private final DeveloperProfileMapper developerProfileMapper;
    private final DevGameMapper devGameMapper;
    @Override
    public Optional<DeveloperProfile> findById(String id) {
        return developerProfileMapper.findById(id);
    }

    @Override
    public Optional<DeveloperProfile> findByUserId(String userId) {
        return developerProfileMapper.findByUserId(userId);
    }

    @Override
    public void syncProjectCount(String developerId) {
        int count = devGameMapper.countByDeveloperId(developerId);
        developerProfileMapper.updateProjectCount(developerId, count);
    }

    @Override
    public List<DeveloperProfile> findAll() {
        return developerProfileMapper.findAll();
    }

    @Override
    public void save(DeveloperProfile developerProfile) {
        developerProfileMapper.insert(developerProfile);
    }

    @Override
    public int batchSave(List<DeveloperProfile> profiles) {
        return developerProfileMapper.batchInsert(profiles);
    }

    @Override
    public int updateProjectCount(String id, Integer projectCount) {
        return developerProfileMapper.updateProjectCountById(id, projectCount);
    }

    @Override
    public int deleteById(String id) {
        return developerProfileMapper.deleteById(id);
    }

    @Override
    public int deleteByUserId(String userId) {
        return developerProfileMapper.deleteByUserId(userId);
    }

    @Override
    public long count() {
        return developerProfileMapper.count();
    }

}
