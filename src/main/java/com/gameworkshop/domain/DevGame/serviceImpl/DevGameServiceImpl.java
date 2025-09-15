package com.gameworkshop.domain.DevGame.serviceImpl;

import com.gameworkshop.domain.DevGame.model.DevGame;
import com.gameworkshop.domain.DevGame.repository.DevGameRepository;
import com.gameworkshop.domain.DevGame.service.DevGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DevGameServiceImpl implements DevGameService {

    private final DevGameRepository devGameRepository;

    @Override
    public void createOrUpdate(DevGame devGame) {
        if (devGame.getName() == null || devGame.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("game name cannot be empty");
        }
        if (devGame.getId() == null && devGame.getReleaseDate() == null) {
            devGame.setReleaseDate(LocalDateTime.now().plusDays(30));
        }
        devGameRepository.save(devGame);
    }

    @Override
    public Optional<DevGame> getGameById(String id) {
        return devGameRepository.findById(id);
    }

    @Override
    public List<DevGame> getGamesByDeveloper(String developerProfileId) {
        if (developerProfileId == null || developerProfileId.trim().isEmpty()) {
            throw new IllegalArgumentException("developer profile id cannot be empty");
        }
        return devGameRepository.findByDeveloperProfileId(developerProfileId);
    }

    @Override
    public boolean removeGameById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("game id cannot be empty");
        }
        return devGameRepository.deleteById(id);
    }

}
