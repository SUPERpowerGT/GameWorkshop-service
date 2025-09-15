package com.gameworkshop.domain.DeveloperProfile.service;

import com.gameworkshop.domain.DeveloperProfile.model.DeveloperProfile;

import java.util.List;

public interface DeveloperProfileService
{
    DeveloperProfile getById(String id);

    void save(DeveloperProfile developerProfile);

    DeveloperProfile getByUserId(String userId);

    int batchSave(List<DeveloperProfile> profiles);
}
