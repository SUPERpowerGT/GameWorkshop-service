package com.gameworkshop.domain.DevGame.service;

import com.gameworkshop.domain.DevGame.model.DevGame;

import java.util.List;
import java.util.Optional;

public interface DevGameService {

    void createOrUpdate(DevGame devGame);

    Optional<DevGame> getGameById(String id);

    List<DevGame> getGamesByDeveloper(String developerProfileId);

    boolean removeGameById(String id);
}
