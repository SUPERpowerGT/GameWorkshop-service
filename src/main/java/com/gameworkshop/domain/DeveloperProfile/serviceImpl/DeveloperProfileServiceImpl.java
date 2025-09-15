package com.gameworkshop.domain.DeveloperProfile.serviceImpl;

import com.gameworkshop.domain.DeveloperProfile.model.DeveloperProfile;
import com.gameworkshop.domain.DeveloperProfile.repository.DeveloperProfileRepository;
import com.gameworkshop.domain.DeveloperProfile.service.DeveloperProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeveloperProfileServiceImpl implements DeveloperProfileService {

    private final DeveloperProfileRepository developerProfileRepository;

    @Override
    public DeveloperProfile getById(String id) {
        return developerProfileRepository.findById(id)
                .orElse(null);
    }

    @Override
    public void save(DeveloperProfile developerProfile) {
        if (developerProfile.getProjectCount() == null) {
            developerProfile.setProjectCount(0);
        }
        developerProfileRepository.save(developerProfile);
    }

    @Override
    public DeveloperProfile getByUserId(String userId) {
        return developerProfileRepository.findByUserId(userId)
                .orElse(null);
    }

    @Override
    public int batchSave(List<DeveloperProfile> profiles) {
        profiles.forEach(profile -> {
            if (profile.getProjectCount() == null) {
                profile.setProjectCount(0);
            }
        });
        return developerProfileRepository.batchSave(profiles);
    }
}
