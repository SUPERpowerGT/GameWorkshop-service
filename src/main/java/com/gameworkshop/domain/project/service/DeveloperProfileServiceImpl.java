package com.gameworkshop.domain.project.service;

import com.gameworkshop.domain.project.model.DeveloperProfile;
import com.gameworkshop.domain.project.repository.DeveloperProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeveloperProfileServiceImpl implements DeveloperProfileService {

    private final DeveloperProfileMapper developerProfileMapper;
    @Autowired
    public DeveloperProfileServiceImpl(DeveloperProfileMapper developerProfileMapper) {
        this.developerProfileMapper = developerProfileMapper;
    }
    @Override
    public DeveloperProfile getById(String id) {
        return developerProfileMapper.findById(id);
    }
    @Override
    public void save(DeveloperProfile developerProfile) {
        developerProfileMapper.insert(developerProfile);
    }
}
