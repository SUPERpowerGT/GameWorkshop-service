package com.gameworkshop.domain.project.service;

import com.gameworkshop.domain.project.model.DeveloperProfile;

public interface DeveloperProfileService
{
    DeveloperProfile getById(String id);
    void save(DeveloperProfile developerProfile);
}
